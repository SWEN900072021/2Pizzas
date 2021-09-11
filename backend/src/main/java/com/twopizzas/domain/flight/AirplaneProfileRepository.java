package com.twopizzas.domain.flight;

import com.twopizzas.data.Repository;
import com.twopizzas.domain.EntityId;

import java.util.List;

public interface AirplaneProfileRepository extends Repository<AirplaneProfile, EntityId> {
    List<AirplaneProfile> findAllAirplanes();
}
