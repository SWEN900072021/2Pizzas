package com.twopizzas.port.data.seatallocation;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.airport.AirportTableResultSetMapper;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.seat.FlightSeatMapper;

import java.util.List;

@Component
public class FlightSeatAllocationMapperImpl implements FlightSeatAllocationMapper {

    static final String TABLE_ALLOCATION = "seatAllocation";
    static final String COLUMN_ID = "id";
    static final String COLUMN_PASSENGER_ID = "passengerId";
    static final String COLUMN_SEAT_ID = "seatId";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_ALLOCATION + "(" + COLUMN_PASSENGER_ID + " , " + COLUMN_SEAT_ID + ")" +
            " VALUES (?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_ALLOCATION +
            " SET " + COLUMN_PASSENGER_ID + " = ?, " + COLUMN_SEAT_ID + " = ?" +
            " WHERE " + COLUMN_ID + " = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_ALLOCATION +
            " WHERE " + COLUMN_ID + " = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_ALLOCATION +
            " WHERE " + COLUMN_ID + " = ?;";

    private final AirportTableResultSetMapper mapper = new AirportTableResultSetMapper();
    private final FlightSeatMapper flightSeatMapper;
    private final ConnectionPool connectionPool;

    @Autowired
    public FlightSeatAllocationMapperImpl(FlightSeatMapper flightSeatMapper, ConnectionPool connectionPool) {
        this.flightSeatMapper = flightSeatMapper;
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(FlightSeatAllocation entity) {
        new SqlStatement(INSERT_TEMPLATE,
                entity.getId().toString(),
                entity.getPassenger().getId().toString(),
                entity.getSeat().getId().toString())
                .doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public FlightSeatAllocation read(EntityId entityId) {
        return null;
    }

    @Override
    public List<FlightSeatAllocation> readAll(FlightSeatAllocationSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(FlightSeatAllocation entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getPassenger().getId().toString(),
                entity.getSeat().getId().toString(),
                entity.getId().toString())
                .doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(FlightSeatAllocation entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString())
                .doExecute(connectionPool.getCurrentTransaction());
    }
}
