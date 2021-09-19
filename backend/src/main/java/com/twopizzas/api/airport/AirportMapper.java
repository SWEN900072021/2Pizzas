package com.twopizzas.api.airport;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.airport.Airport;
import org.mapstruct.Mapper;

import java.time.ZoneId;

@Mapper(uses = BaseMapper.class)
public interface AirportMapper {
    AirportDto map(Airport source);
    default String map(ZoneId source) {
        return source.toString();
    }
}
