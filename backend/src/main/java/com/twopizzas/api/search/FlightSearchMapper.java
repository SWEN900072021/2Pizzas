package com.twopizzas.api.search;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = BaseMapper.class)
public interface FlightSearchMapper {

    @Mapping(source = "airplaneProfile", target = "profile")
    @Mapping(target = "seats", source = "source", qualifiedByName = "mapSeats")
    FlightSearchResultDto map(Flight source);

    @Named("mapSeats")
    default List<FlightSearchResultDto.Seat> mapSeats(Flight source) {
        Set<FlightSeat> booked = source.getAllocatedSeats().stream().map(FlightSeatAllocation::getSeat).collect(Collectors.toSet());
        return source.getSeats().stream().map(s -> {
            FlightSearchResultDto.Seat seat = new FlightSearchResultDto.Seat();
            seat.setSeatClass(s.getSeatClass());
            seat.setName(s.getName());
            seat.setBooked(booked.contains(s));
            return seat;
        }).collect(Collectors.toList());
    }
}
