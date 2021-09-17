package com.twopizzas.api.search;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.flight.Flight;
import org.mapstruct.Mapper;

@Mapper(uses = BaseMapper.class)
public interface FlightSearchMapper {

    FlightSearchResultDto map(Flight source);
}
