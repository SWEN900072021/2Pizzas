package com.twopizzas.port.data.airport;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;

@Component
public class AirportRepository extends AbstractRepository<Airport, EntityId, AirportSpecification, AirportMapper> {

    @Autowired
    public AirportRepository(AirportMapper dataMapper) {
        super(dataMapper);
    }
}
