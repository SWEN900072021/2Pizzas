package com.twopizzas.port.data.seat;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.port.data.db.ConnectionPool;

public interface FlightSeatSpecification extends Specification<FlightSeat, ConnectionPool> {
}
