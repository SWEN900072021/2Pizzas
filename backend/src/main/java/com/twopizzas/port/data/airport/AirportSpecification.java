package com.twopizzas.port.data.airport;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.port.data.db.ConnectionPool;

public interface AirportSpecification extends Specification<Airport, ConnectionPool> {
}
