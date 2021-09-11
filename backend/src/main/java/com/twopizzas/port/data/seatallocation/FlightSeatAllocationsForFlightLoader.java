package com.twopizzas.port.data.seatallocation;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class FlightSeatAllocationsForFlightLoader implements ValueLoader<List<FlightSeatAllocation>> {

    private static final String TEMPLATE =
            "SELECT * FROM (seatAllocation JOIN seat ON seatAllocation.seatId = seat.id) WHERE seat.flightId = ?;";

    private final ConnectionPool connectionPool;
    private final FlightSeatAllocationMapper mapper;
    private final EntityId flightId;

    public FlightSeatAllocationsForFlightLoader(ConnectionPool connectionPool, FlightSeatAllocationMapper mapper, EntityId flightId) {
        this.connectionPool = connectionPool;
        this.mapper = mapper;
        this.flightId = flightId;
    }

    @Override
    public ValueHolder<List<FlightSeatAllocation>> load() {
        return BaseValueHolder.of(mapper.map(
                new SqlStatement(TEMPLATE, flightId.toString())
                        .doQuery(connectionPool.getCurrentTransaction())
        ));
    }
}
