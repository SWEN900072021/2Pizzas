package com.twopizzas.port.data.seat;

import com.twopizzas.data.LazyValueHolderProxy;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.SeatClass;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.flight.FlightByIdLoader;
import com.twopizzas.port.data.flight.FlightMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
class FlightSeatMapperImpl implements FlightSeatMapper {

    static final String TABLE_SEAT = "seat";
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_FLIGHT_ID = "flightId";
    static final String COLUMN_CLASS = "class";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_SEAT + "(" + COLUMN_ID + " , " + COLUMN_NAME + ", " + COLUMN_FLIGHT_ID + ", " + COLUMN_CLASS + ")" +
            " VALUES (?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_SEAT +
            " SET " + COLUMN_NAME + " = ?, " + COLUMN_FLIGHT_ID + " = ?, " + COLUMN_CLASS + " = ?" +
            " WHERE " + COLUMN_ID + " = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_SEAT +
            " WHERE " + COLUMN_ID + " = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_SEAT +
            " WHERE " + COLUMN_ID + " = ?;";

    private final FlightMapper flightMapper;
    private final ConnectionPool connectionPool;

    @Autowired
    public FlightSeatMapperImpl(FlightMapper flightMapper, ConnectionPool connectionPool) {
        this.flightMapper = flightMapper;
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(FlightSeat entity) {
        new SqlStatement(INSERT_TEMPLATE,
                entity.getId().toString(),
                entity.getName(),
                entity.getFlight().getId().toString(),
                entity.getSeatClass().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public FlightSeat read(EntityId entityId) {
        return map(new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction()))
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public List<FlightSeat> readAll(FlightSeatSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(FlightSeat entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getName(),
                entity.getFlight().getId().toString(),
                entity.getSeatClass().toString(),
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(FlightSeat entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public List<FlightSeat> map(ResultSet resultSet) {
        List<FlightSeat> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                FlightSeat one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Airport.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }

    @Override
    public FlightSeat mapOne(ResultSet resultSet) {
        try {
            return new FlightSeat(
                    EntityId.of(resultSet.getObject(COLUMN_ID, String.class)),
                    resultSet.getObject(COLUMN_NAME, String.class),
                    SeatClass.valueOf(resultSet.getObject(COLUMN_CLASS, String.class)),
                    LazyValueHolderProxy.makeLazy(
                            new FlightByIdLoader(flightMapper, resultSet.getObject(COLUMN_FLIGHT_ID, String.class))
                    )
            );
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Airport.class.getName(), e.getMessage()),
                    e);
        }
    }
}
