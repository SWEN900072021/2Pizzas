package com.twopizzas.api.flight;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = BaseMapper.class)
public interface FlightMapper {

    @Mapping(source = "airplaneProfile", target = "profile")
    FlightDto map(Flight source);

    @Mapping(source = "seat.name", target = "seatName")
    @Mapping(source = "passenger.surname", target = "surname")
    @Mapping(source = "passenger.givenName", target = "givenName")
    @Mapping(source = "passenger.passportNumber", target = "passportNumber")
    @Mapping(source = "passenger.nationality", target = "nationality")
    @Mapping(source = "passenger.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "passenger.booking.id", target = "booking")
    FlightPassengerDto map(FlightSeatAllocation source);

}
