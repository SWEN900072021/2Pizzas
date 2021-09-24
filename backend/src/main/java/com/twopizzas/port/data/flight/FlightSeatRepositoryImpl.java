package com.twopizzas.port.data.flight;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.port.data.seat.FlightSeatMapper;
import com.twopizzas.port.data.seat.FlightSeatSpecification;

@Component
public class FlightSeatRepositoryImpl extends AbstractRepository<FlightSeat, FlightSeatSpecification, FlightSeatMapper> {
    @Autowired
    public FlightSeatRepositoryImpl(FlightSeatMapper dataMapper) {
        super(dataMapper);
    }
}
