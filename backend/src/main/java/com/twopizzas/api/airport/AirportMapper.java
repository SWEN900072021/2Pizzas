package com.twopizzas.api.airport;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.airport.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.ZoneId;

@Mapper(uses = BaseMapper.class)
public interface AirportMapper {

    @Mapping(source = "utcOffset", target = "zoneId")
    AirportDto map(Airport source);

    default String map(ZoneId source) {
        if (source == null )
            return  null;

        return source.toString();
    }
}
