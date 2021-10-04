package com.twopizzas.port.data.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.LazyValueHolderProxy;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.OptimisticLockingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.airplane.AirplaneProfileMapper;
import com.twopizzas.port.data.airport.AirportMapper;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.seat.AllSeatsForFlightSpecification;
import com.twopizzas.port.data.seat.FlightSeatMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationsForFlightLoader;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
class FlightMapperImpl implements FlightMapper {

    static final String TABLE_FLIGHT = "flight";
    static final String COLUMN_ID = "id";
    static final String COLUMN_CODE = "code";
    static final String COLUMN_DEPARTURE = "departure";
    static final String COLUMN_ARRIVAL= "arrival";
    static final String COLUMN_ORIGIN = "origin";
    static final String COLUMN_DESTINATION = "destination";
    static final String COLUMN_AIRLINE_ID = "airlineId";
    static final String COLUMN_AIRPLANE_ID = "airplaneId";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_FIRST_CLASS_COST = "firstClassCost";
    static final String COLUMN_BUSINESS_CLASS_COST = "businessClassCost";
    static final String COLUMN_ECONOMY_CLASS_COST = "economyClassCost";
    static final String COLUMN_VERSION = "version";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_FLIGHT + "(" + COLUMN_ID + " , " + COLUMN_CODE + ", " + COLUMN_DEPARTURE + ", " + COLUMN_ARRIVAL + ", " + COLUMN_ORIGIN + ", " + COLUMN_DESTINATION + ", " + COLUMN_AIRLINE_ID + ", " + COLUMN_AIRPLANE_ID + ", " + COLUMN_STATUS + ", " + COLUMN_FIRST_CLASS_COST + ", " + COLUMN_BUSINESS_CLASS_COST + ", " + COLUMN_ECONOMY_CLASS_COST + ", " + COLUMN_VERSION + ")" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_FLIGHT +
            " SET " + COLUMN_CODE + " = ?, " + COLUMN_DEPARTURE + " = ?, " + COLUMN_ARRIVAL + " = ?, " + COLUMN_ORIGIN + " = ?, " + COLUMN_DESTINATION + " = ?, " + COLUMN_AIRLINE_ID + " = ?, " + COLUMN_AIRPLANE_ID + " = ?, " + COLUMN_STATUS + " = ?, " + COLUMN_FIRST_CLASS_COST + " = ?, " + COLUMN_BUSINESS_CLASS_COST + " = ?, " + COLUMN_ECONOMY_CLASS_COST + " = ?" + COLUMN_VERSION + " = ?" +
            " WHERE id = ? AND version = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_FLIGHT +
            " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_FLIGHT +
            " WHERE id = ?;";

    private static final String DELETE_STOPOVERS_TEMPLATE =
            "DELETE FROM " + FlightStopOversLoader.TABLE_STOPOVER +
            " WHERE flightId = ?;";

    private static final String INSERT_STOPOVER_TEMPLATE =
            "INSERT INTO " + FlightStopOversLoader.TABLE_STOPOVER +
            " (" + FlightStopOversLoader.COLUMN_FLIGHT_ID + ", " + FlightStopOversLoader.COLUMN_ARRIVAL + ", " + FlightStopOversLoader.COLUMN_DEPARTURE + ", " + FlightStopOversLoader.COLUMN_AIRPORT_ID + ")" +
            " VALUES (?, ?, ?, ?);";

    private static final String DELETE_ALLOCATIONS_TEMPLATE =
            "DELETE FROM seatAllocation" +
            " WHERE EXISTS" +
            " (SELECT 1 FROM seatAllocation JOIN seat ON seat.id = seatAllocation.seatId" +
            " WHERE seat.flightId = ?);";

    private static final String INSERT_ALLOCATION_TEMPLATE =
            "INSERT INTO seatAllocation" +
            " (passengerId, seatId)" +
            " VALUES (?, ?);";

    private final ConnectionPool connectionPool;
    private final FlightSeatMapper flightSeatMapper;
    private final AirlineMapper airlineMapper;
    private final FlightSeatAllocationMapper flightSeatAllocationMapper;
    private final AirplaneProfileMapper airplaneProfileMapper;
    private final AirportMapper airportMapper;

    @Autowired
    protected FlightMapperImpl(ConnectionPool connectionPool, FlightSeatMapper flightSeatMapper, AirlineMapper airlineMapper, FlightSeatAllocationMapper flightSeatAllocationMapper, AirplaneProfileMapper airplaneProfileMapper, AirportMapper airportMapper) {
        this.connectionPool = connectionPool;
        this.flightSeatMapper = flightSeatMapper;
        this.airlineMapper = airlineMapper;
        this.flightSeatAllocationMapper = flightSeatAllocationMapper;
        this.airplaneProfileMapper = airplaneProfileMapper;
        this.airportMapper = airportMapper;
    }

    @Override
    public void create(Flight entity) {
        new SqlStatement(INSERT_TEMPLATE,
                entity.getId().toString(),
                entity.getCode(),
                entity.getDeparture(),
                entity.getArrival(),
                entity.getOrigin().getId().toString(),
                entity.getDestination().getId().toString(),
                entity.getAirline().getId().toString(),
                entity.getAirplaneProfile().getId().toString(),
                entity.getStatus().toString(),
                entity.getFirstClassCost(),
                entity.getBusinessClassCost(),
                entity.getEconomyClassCost(),
                entity.getVersion()
        ).doExecute(connectionPool.getCurrentTransaction());

        insertAllocations(entity.getAllocatedSeats());
        insertStopOvers(entity);
    }

    @Override
    public Flight read(EntityId entityId) {
        return map(new SqlStatement(SELECT_TEMPLATE,
                entityId.toString()
        ).doQuery(connectionPool.getCurrentTransaction())).stream().findFirst().orElse(null);
    }

    @Override
    public List<Flight> readAll(FlightSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Flight entity) {
        long updated = new SqlStatement(UPDATE_TEMPLATE,
                entity.getCode(),
                entity.getDeparture().withOffsetSameInstant(ZoneOffset.UTC),
                entity.getArrival().withOffsetSameInstant(ZoneOffset.UTC),
                entity.getOrigin().getId().toString(),
                entity.getDestination().getId().toString(),
                entity.getAirline().getId().toString(),
                entity.getAirplaneProfile().getId().toString(),
                entity.getStatus().toString(),
                entity.getFirstClassCost(),
                entity.getBusinessClassCost(),
                entity.getEconomyClassCost(),
                entity.getVersion() + 1,
                entity.getId().toString(),
                entity.getVersion()
        ).doUpdate(connectionPool.getCurrentTransaction());

        if (updated == 0) {
            throw new OptimisticLockingException();
        }

        List<FlightSeatAllocation> allocations = entity.getAllocatedSeats();
        deleteAllocations(entity);
        insertAllocations(allocations);

        deleteStopOvers(entity);
        insertStopOvers(entity);
    }

    @Override
    public void delete(Flight entity) {
        deleteStopOvers(entity);
        deleteAllocations(entity);

        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public List<Flight> map(ResultSet resultSet) {
        List<Flight> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Flight one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", getEntityClass().getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }

    @Override
    public Flight mapOne(ResultSet resultSet) {
        try {
            EntityId flightId = EntityId.of(resultSet.getObject(COLUMN_ID, String.class));
            return new Flight(
                    flightId,
                    LazyValueHolderProxy.makeLazy(new FlightSeatAllocationsForFlightLoader(connectionPool, flightSeatAllocationMapper, flightId)),
                    airplaneProfileMapper.read(EntityId.of(resultSet.getObject(COLUMN_AIRPLANE_ID, String.class))),
                    airlineMapper.read(EntityId.of(resultSet.getObject(COLUMN_AIRLINE_ID, String.class))),
                    BaseValueHolder.of(flightSeatMapper.readAll(new AllSeatsForFlightSpecification(flightSeatMapper, flightId))),
                    airportMapper.read(EntityId.of(resultSet.getObject(COLUMN_ORIGIN, String.class))),
                    airportMapper.read(EntityId.of(resultSet.getObject(COLUMN_DESTINATION, String.class))),
                    resultSet.getObject(COLUMN_DEPARTURE, OffsetDateTime.class).withOffsetSameInstant(ZoneOffset.UTC),
                    resultSet.getObject(COLUMN_ARRIVAL, OffsetDateTime.class).withOffsetSameInstant(ZoneOffset.UTC),
                    new FlightStopOversLoader(connectionPool, airportMapper, flightId).load().get(),
                    resultSet.getObject(COLUMN_CODE, String.class),
                    Flight.FlightStatus.valueOf(resultSet.getObject(COLUMN_STATUS, String.class)),
                            resultSet.getObject(COLUMN_FIRST_CLASS_COST, BigDecimal.class),
                            resultSet.getObject(COLUMN_BUSINESS_CLASS_COST, BigDecimal.class),
                            resultSet.getObject(COLUMN_ECONOMY_CLASS_COST, BigDecimal.class),
                    resultSet.getObject(COLUMN_VERSION, long.class)
            );
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", getEntityClass().getName(), e.getMessage()),
                    e);
        }
    }

    private void insertAllocations(List<FlightSeatAllocation> allocations) {
        allocations.forEach(
                a -> new SqlStatement(INSERT_ALLOCATION_TEMPLATE,
                        a.getPassenger().getId().toString(),
                        a.getSeat().getId().toString()
                ).doExecute(connectionPool.getCurrentTransaction()));
    }

    private void deleteAllocations(Flight flight) {
        new SqlStatement(DELETE_ALLOCATIONS_TEMPLATE,
                flight.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    private void insertStopOvers(Flight entity) {
        entity.getStopOvers().forEach(
                s -> new SqlStatement(INSERT_STOPOVER_TEMPLATE,
                        entity.getId().toString(),
                        s.getArrival().withOffsetSameInstant(ZoneOffset.UTC),
                        s.getDeparture().withOffsetSameInstant(ZoneOffset.UTC),
                        s.getLocation().getId().toString()
                ).doExecute(connectionPool.getCurrentTransaction()));
    }

    private void deleteStopOvers(Flight entity) {
        new SqlStatement(DELETE_STOPOVERS_TEMPLATE, entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }
}
