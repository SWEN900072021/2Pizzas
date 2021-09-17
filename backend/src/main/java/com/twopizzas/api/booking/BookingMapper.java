package com.twopizzas.api.booking;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.booking.Booking;
import org.mapstruct.Mapper;

@Mapper(uses = BaseMapper.class)
public interface BookingMapper {
    BookingDto map(Booking source);
}
