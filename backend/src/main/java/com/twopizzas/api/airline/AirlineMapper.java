package com.twopizzas.api.airline;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.user.Airline;
import org.mapstruct.Mapper;

@Mapper(uses = BaseMapper.class)
public interface AirlineMapper {
    AirlineDto map(Airline source);
}
