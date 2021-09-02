package com.twopizzas.port.data.airport;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.Airport;
import com.twopizzas.port.data.AbstractSqlSpecification;

public abstract class AirportSpecification extends AbstractSqlSpecification<Airport> implements Specification<Airport> {
    protected AirportSpecification(AirportMapper dataMapper) {
        super(dataMapper);
    }
}
