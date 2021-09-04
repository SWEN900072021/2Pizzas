package com.twopizzas.port.data.seatallocation;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.db.ConnectionPool;

public interface FlightSeatAllocationSpecification extends Specification<FlightSeatAllocation, ConnectionPool> {
}
