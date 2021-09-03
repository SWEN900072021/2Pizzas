package com.twopizzas.port.data.flight;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.Flight;
import com.twopizzas.port.data.db.ConnectionPool;

public interface FlightSpecification extends Specification<Flight, ConnectionPool> {
}