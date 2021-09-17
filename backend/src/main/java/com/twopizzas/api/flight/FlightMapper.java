package com.twopizzas.api.flight;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import org.mapstruct.Mapper;

@Mapper(uses = BaseMapper.class)
public interface FlightMapper {

    FlightDto map(Flight source);

    FlightPassengerDto map(FlightSeatAllocation source);

}
