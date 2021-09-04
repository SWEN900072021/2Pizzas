package com.twopizzas.port.data.seatallocation;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeatAllocation;

public interface FlightSeatAllocationMapper extends DataMapper<FlightSeatAllocation, EntityId, FlightSeatAllocationSpecification> {
    @Override
    default Class<FlightSeatAllocation> getEntityClass() {
        return FlightSeatAllocation.class;
    }
}
