package com.twopizzas.port.data.airline;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.port.data.db.ConnectionPool;

public interface AirlineSpecification extends Specification<Airline, ConnectionPool> {
}
