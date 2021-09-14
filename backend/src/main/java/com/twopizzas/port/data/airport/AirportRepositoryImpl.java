package com.twopizzas.port.data.airport;

import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.AirportRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
public class AirportRepositoryImpl extends AbstractRepository<Airport, AirportSpecification, AirportMapper> implements AirportRepository {

    @Autowired
    public AirportRepositoryImpl(AirportMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Airport> findAllAirports() {
        return dataMapper.readAll(new AllAirportsSpecification(dataMapper));
    }
}
