package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.List;

public interface AirplaneRepository extends Repository<Airplane, EntityId> {
    List<Airplane> findAllAirplanes();
}
