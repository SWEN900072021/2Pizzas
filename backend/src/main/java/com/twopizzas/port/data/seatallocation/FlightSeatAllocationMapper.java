package com.twopizzas.port.data.seatallocation;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface FlightSeatAllocationMapper extends DataMapper<FlightSeatAllocation, EntityId, FlightSeatAllocationSpecification>, SqlResultSetMapper<FlightSeatAllocation> {
    @Override
    default Class<FlightSeatAllocation> getEntityClass() {
        return FlightSeatAllocation.class;
    }
}
