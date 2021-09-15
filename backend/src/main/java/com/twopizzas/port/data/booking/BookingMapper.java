package com.twopizzas.port.data.booking;

import com.twopizzas.data.DataMapper;
import com.twopizzas.di.Component;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.SqlResultSetMapper;


@Component
public interface BookingMapper extends DataMapper<Booking, EntityId, BookingSpecification>, SqlResultSetMapper<Booking> {
    @Override
    default Class<Booking> getEntityClass() {
        return Booking.class;
    }
}
