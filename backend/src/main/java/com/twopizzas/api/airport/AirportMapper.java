package com.twopizzas.api.airport;

import com.twopizzas.domain.airport.Airport;
import org.mapstruct.Mapper;

@Mapper
public interface AirportMapper {
    AirportDto map(Airport source);
}
