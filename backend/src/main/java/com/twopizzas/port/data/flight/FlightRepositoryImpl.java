package com.twopizzas.port.data.flight;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightRepository;
import com.twopizzas.domain.flight.FlightSearch;

import java.util.List;

@Component
public class FlightRepositoryImpl extends AbstractRepository<Flight, EntityId, FlightSpecification, FlightMapper> implements FlightRepository {

    @Autowired
    public FlightRepositoryImpl(FlightMapper dataMapper) {
        super(dataMapper);
    }

    @Override
    public List<Flight> searchFlights(FlightSearch search) {
        return null;
    }
}
