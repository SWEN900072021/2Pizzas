package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.List;

public interface AirportRepository extends Repository<Airport, EntityId> {
    List<Airport> findAllAirports();
}
