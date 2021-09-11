package com.twopizzas.port.data.booking;

import com.twopizzas.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.*;

import java.util.List;

@Component
class BookingRepositoryImpl extends AbstractRepository<Booking, EntityId, BookingSpecification, BookingMapper> implements BookingRepository {

    @Autowired
    public BookingRepositoryImpl(BookingMapper mapper) {
        super(mapper);
    }

    public List<Booking> findAllFlightBookings(EntityId flightId) {
        return dataMapper.readAll(new FlightBookingsSpecification(flightId, dataMapper));
    }
}
