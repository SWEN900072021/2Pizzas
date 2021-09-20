package com.twopizzas.api.airplaneProfile;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.flight.AirplaneProfile;
import org.mapstruct.Mapper;

@Mapper(uses = BaseMapper.class)
public interface AirplaneProfileMapper {
    AirplaneProfileDto map(AirplaneProfile source);
}
