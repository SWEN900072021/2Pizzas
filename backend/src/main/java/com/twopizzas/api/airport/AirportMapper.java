package com.twopizzas.api.airport;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.airport.Airport;
import org.mapstruct.Mapper;

@Mapper(uses = BaseMapper.class)
public interface AirportMapper {
    AirportDto map(Airport source);
}
