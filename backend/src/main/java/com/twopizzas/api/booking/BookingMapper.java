package com.twopizzas.api.booking;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.domain.flight.SeatBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = BaseMapper.class)
public interface BookingMapper {

    @Mapping(source = "totalCost", target = "totalCost")
    @Mapping(source = "date", target = "dateTime")
    @Mapping(source = "flightBooking", target = "flight")
    @Mapping(source = "returnFlightBooking", target = "returnFlight")
    BookingDto map(Booking source);

    @Mapping(source = "flight.id", target = "id")
    @Mapping(source = "flight.status", target = "status")
    @Mapping(source = "flight.origin", target = "origin")
    @Mapping(source = "flight.destination", target = "destination")
    @Mapping(source = "flight.departure", target = "departure")
    @Mapping(source = "flight.arrival", target = "arrival")
    @Mapping(source = "flight.airplaneProfile", target = "profile")
    @Mapping(source = "flight.airline", target = "airline")
    @Mapping(source = "flight.code", target = "code")
    @Mapping(source = "allocations", target = "seatAllocations")
    BookingDto.Flight map(SeatBooking source);

    @Mapping(source = "seat.name", target = "seat")
    @Mapping(source = "seat.seatClass", target = "seatClass")
    @Mapping(source = "passenger.givenName", target = "givenName")
    @Mapping(source = "passenger.surname", target = "surname")
    BookingDto.SeatAllocation map(FlightSeatAllocation source);
}
