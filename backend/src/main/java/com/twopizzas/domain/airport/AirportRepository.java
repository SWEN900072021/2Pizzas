package com.twopizzas.domain.airport;

import com.twopizzas.data.Repository;
import com.twopizzas.domain.EntityId;

import java.util.List;

public interface AirportRepository extends Repository<Airport, EntityId> {
    List<Airport> findAllAirports();
}
