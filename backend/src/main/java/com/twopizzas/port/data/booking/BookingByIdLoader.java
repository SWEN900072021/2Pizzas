package com.twopizzas.port.data.booking;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;

public class BookingByIdLoader implements ValueLoader<Booking> {

    private final BookingMapper mapper;
    private final EntityId bookingId;

    public BookingByIdLoader(BookingMapper mapper, EntityId bookingId) {
        this.mapper = mapper;
        this.bookingId = bookingId;
    }

    @Override
    public ValueHolder<Booking> load() {
        return BaseValueHolder.of(mapper.read(bookingId));
    }
}
