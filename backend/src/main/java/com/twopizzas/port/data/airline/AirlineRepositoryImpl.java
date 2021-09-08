package com.twopizzas.port.data.airline;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Airline;
import com.twopizzas.domain.AirlineRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
public class AirlineRepositoryImpl extends AbstractRepository<Airline, EntityId, AirlineSpecification, AirlineMapper> implements AirlineRepository {

    @Autowired
    public AirlineRepositoryImpl(AirlineMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Airline> findAllAirlines() {
        return dataMapper.readAll(new AllAirlinesSpecification());
    }
}
