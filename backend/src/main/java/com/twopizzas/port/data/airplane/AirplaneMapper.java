package com.twopizzas.port.data.airplane;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.AirplaneProfile;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface AirplaneMapper extends DataMapper<Airplane, EntityId, AirplaneSpecification> {
    @Override
    default Class<Airplane> getEntityClass() {
        return Airplane.class;
    }
}
