package com.twopizzas.api.flight;

import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import org.mapstruct.Mapper;

@Mapper
public interface FlightMapper {

    FlightDto map(Flight source);

    FlightPassengerDto map(FlightSeatAllocation source);

}
