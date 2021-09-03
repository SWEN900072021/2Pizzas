package com.twopizzas.port.data.flight;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Flight;
import com.twopizzas.domain.FlightRepository;
import com.twopizzas.domain.FlightSearch;

import java.util.List;

@Component
public class FlightRepositoryImpl extends AbstractRepository<Flight, EntityId, FlightSpecification, FlightMapper> implements FlightRepository {

    @Autowired
    public FlightRepositoryImpl(FlightMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Flight> customerFlights(EntityId customerId) {
        return dataMapper.readAll(new CustomerFlightsSpecification(customerId));
    }

    @Override
    public List<Flight> searchFlights(FlightSearch search) {
        return null;
    }
}
