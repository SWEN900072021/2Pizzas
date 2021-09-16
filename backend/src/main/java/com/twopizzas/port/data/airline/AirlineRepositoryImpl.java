package com.twopizzas.port.data.airline;

import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.user.AirlineRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
class AirlineRepositoryImpl extends AbstractRepository<Airline, AirlineSpecification, AirlineMapper> implements AirlineRepository {

    @Autowired
    public AirlineRepositoryImpl(AirlineMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Airline> findAllAirlines() {
        return dataMapper.readAll(new AllAirlinesSpecification(dataMapper));
    }
}
