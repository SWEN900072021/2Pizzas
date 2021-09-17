package com.twopizzas.port.data.airport;

import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.airport.AirportRepository;

import java.util.List;

@Component
class AirportRepositoryImpl extends AbstractRepository<Airport, AirportSpecification, AirportMapper> implements AirportRepository {

    @Autowired
    public AirportRepositoryImpl(AirportMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Airport> findAllAirports() {
        return dataMapper.readAll(new AllAirportsSpecification(dataMapper));
    }
}
