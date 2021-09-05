package com.twopizzas.port.data.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.LazyValueHolderProxy;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.airline.AirlineMapper;
import com.twopizzas.port.data.airplane.AirplaneProfileMapper;
import com.twopizzas.port.data.airport.AirportMapper;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.seat.AllSeatsForFlightSpecification;
import com.twopizzas.port.data.seat.FlightSeatMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationResultsMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationsForFlightLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlightMapperImpl implements FlightMapper {

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

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_FLIGHT + "(" + COLUMN_ID + " , " + COLUMN_CODE + ", " + COLUMN_DEPARTURE + ", " + COLUMN_ARRIVAL + ", " + COLUMN_ORIGIN + ", " + COLUMN_DESTINATION + ", " + COLUMN_AIRLINE_ID + ", " + COLUMN_AIRPLANE_ID + ", " + COLUMN_STATUS +")" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_FLIGHT +
            " SET " + COLUMN_CODE + " = ?, " + COLUMN_DEPARTURE + " = ?, " + COLUMN_ARRIVAL + " = ?, " + COLUMN_ORIGIN + " = ?" + COLUMN_DESTINATION + " = ?" + COLUMN_AIRLINE_ID + " = ?" + COLUMN_AIRPLANE_ID + " = ?" + COLUMN_STATUS + " = ?" +
            " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_FLIGHT +
            " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_FLIGHT +
            " WHERE id = ?;";

    private final ConnectionPool connectionPool;
    private final FlightSeatMapper flightSeatMapper;
    private final AirlineMapper airlineMapper;
    private final FlightSeatAllocationResultsMapper flightSeatAllocationMapper;
    private final AirplaneProfileMapper airplaneProfileMapper;
    private final AirportMapper airportMapper;

    @Autowired
    protected FlightMapperImpl(ConnectionPool connectionPool, FlightSeatMapper flightSeatMapper, AirlineMapper airlineMapper, FlightSeatAllocationResultsMapper flightSeatAllocationMapper, AirplaneProfileMapper airplaneProfileMapper, AirportMapper airportMapper) {
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
                entity.getStatus().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
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
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getCode(),
                entity.getDeparture(),
                entity.getArrival(),
                entity.getOrigin().getId().toString(),
                entity.getDestination().getId().toString(),
                entity.getAirline().getId().toString(),
                entity.getAirplaneProfile().getId().toString(),
                entity.getStatus().toString(),
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Flight entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public List<Flight> map(ResultSet resultSet) {
        List<Flight> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                EntityId flightId = EntityId.of(resultSet.getObject(COLUMN_ID, String.class));
                mapped.add(new Flight(
                        flightId,
                        LazyValueHolderProxy.makeLazy(new FlightSeatAllocationsForFlightLoader(connectionPool, flightSeatAllocationMapper, flightId)),
                        airplaneProfileMapper.read(EntityId.of(resultSet.getObject(COLUMN_AIRPLANE_ID, String.class))),
                        airlineMapper.read(EntityId.of(resultSet.getObject(COLUMN_AIRLINE_ID, String.class))),
                        BaseValueHolder.of(flightSeatMapper.readAll(new AllSeatsForFlightSpecification(flightSeatMapper, flightId))),
                        airportMapper.read(EntityId.of(resultSet.getObject(COLUMN_ORIGIN, String.class))),
                        airportMapper.read(EntityId.of(resultSet.getObject(COLUMN_DESTINATION, String.class))),
                        resultSet.getObject(COLUMN_DEPARTURE, OffsetDateTime.class),
                        resultSet.getObject(COLUMN_ARRIVAL, OffsetDateTime.class),
                        new FlightStopOversLoader(connectionPool, airportMapper, this, flightId).load().get(),
                        resultSet.getObject(COLUMN_CODE, String.class),
                        Flight.Status.valueOf(resultSet.getObject(COLUMN_STATUS, String.class))
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Airport.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }
}
