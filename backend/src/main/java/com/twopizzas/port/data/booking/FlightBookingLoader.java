package com.twopizzas.port.data.booking;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.LazyValueHolderProxy;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;

import java.util.List;

public class FlightBookingLoader implements LazyValueHolderProxy.ValueLoader<List<Booking>> {

    private BookingMapper mapper;
    private EntityId flightId;

    @Override
    public ValueHolder<List<Booking>> load() {
        // go to database
        FlightBookingsSpecification spec = new FlightBookingsSpecification(flightId);
        List<Booking> bookings = mapper.readAll(spec);
        return new BaseValueHolder<>(bookings);
    }
}
