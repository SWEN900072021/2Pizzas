package com.twopizzas.port.data.seat;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.FlightSeatRepository;
import com.twopizzas.port.data.AbstractRepository;

@Component
public class FlightSeatRepositoryImpl extends AbstractRepository<FlightSeat, FlightSeatSpecification, FlightSeatMapper> implements FlightSeatRepository {
    @Autowired
    public FlightSeatRepositoryImpl(FlightSeatMapper dataMapper) {
        super(dataMapper);
    }
}
