package com.twopizzas.port.data.passenger;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.domain.booking.PassengerRepository;
import com.twopizzas.port.data.AbstractRepository;

@Component
public class PassengerRepositoryImpl extends AbstractRepository<Passenger, PassengerSpecification, PassengerMapper> implements PassengerRepository {

    @Autowired
    public PassengerRepositoryImpl(PassengerMapper dataMapper) {
        super(dataMapper);
    }
}
