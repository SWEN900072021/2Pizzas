package com.twopizzas.api.search;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.domain.flight.SeatClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(uses = BaseMapper.class)
public interface FlightSearchMapper {

    @Mapping(source = "airplaneProfile", target = "profile")
    @Mapping(target = "seats", source = "source", qualifiedByName = "mapSeats")
    @Mapping(target = "seatAvailabilities", source = "source", qualifiedByName = "mapAvailabilities")
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

    @Named("mapAvailabilities")
    default List<FlightSearchResultDto.SeatAvailability> mapAvailabilities(Flight source) {
        Set<String> booked = source.getAllocatedSeats().stream().map(FlightSeatAllocation::getSeat).map(FlightSeat::getName).collect(Collectors.toSet());
        Set<FlightSeat> available = source.getSeats().stream().filter(s -> !booked.contains(s.getName())).collect(Collectors.toSet());

        Map<SeatClass, List<String>> seatMap = new HashMap<>();
        available.forEach(s -> {
            if (!seatMap.containsKey(s.getSeatClass())) {
                seatMap.put(s.getSeatClass(), new ArrayList<>());
            }
            seatMap.get(s.getSeatClass()).add(s.getName());
        });

        return seatMap.entrySet().stream()
                .map((entry-> new FlightSearchResultDto.SeatAvailability().setSeatClass(entry.getKey()).setSeats(entry.getValue())))
                .collect(Collectors.toList());
    }
}
