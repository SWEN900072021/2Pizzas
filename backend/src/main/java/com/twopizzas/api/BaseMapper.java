package com.twopizzas.api;

import com.twopizzas.domain.EntityId;
import org.mapstruct.Mapper;

@Mapper
public interface BaseMapper {

    default String map(EntityId entityId) {
        if (entityId == null || entityId.getValue() == null) {
            return null;
        }
        return entityId.getValue().toString();
    }

}
