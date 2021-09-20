package com.twopizzas.port.data.booking;

import com.twopizzas.port.data.AbstractRepository;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.domain.booking.BookingRepository;
import com.twopizzas.domain.EntityId;

import java.util.List;

@Component
class BookingRepositoryImpl extends AbstractRepository<Booking, BookingSpecification, BookingMapper> implements BookingRepository {

    @Autowired
    public BookingRepositoryImpl(BookingMapper mapper) {
        super(mapper);
    }

    public List<Booking> findAllFlightBookings(EntityId flightId) {
        return doSpecification(new FlightBookingsSpecification(flightId, dataMapper));
    }

    @Override
    public List<Booking> findAllCustomerBookings(EntityId customerId) {
        return doSpecification(new CustomerBookingsSpecification(customerId, dataMapper));
    }
}
