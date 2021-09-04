package com.twopizzas.port.data.airplane;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.AirplaneProfile;

public interface AirplaneProfileMapper extends DataMapper<AirplaneProfile, EntityId, AirplaneProfileSpecification> {
    @Override
    default Class<AirplaneProfile> getEntityClass() {
        return AirplaneProfile.class;
    }
}
