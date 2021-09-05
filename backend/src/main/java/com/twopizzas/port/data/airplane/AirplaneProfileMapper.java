package com.twopizzas.port.data.airplane;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface AirplaneProfileMapper extends DataMapper<AirplaneProfile, EntityId, AirplaneProfileSpecification>, SqlResultSetMapper<AirplaneProfile> {
    @Override
    default Class<AirplaneProfile> getEntityClass() {
        return AirplaneProfile.class;
    }
}
