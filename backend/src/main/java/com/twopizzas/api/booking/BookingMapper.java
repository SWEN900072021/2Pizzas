package com.twopizzas.api.booking;

import com.twopizzas.domain.booking.Booking;
import org.mapstruct.Mapper;

@Mapper
public interface BookingMapper {
    BookingDto map(Booking source);
}
