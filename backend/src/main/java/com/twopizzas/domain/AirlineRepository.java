package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.List;

public interface AirlineRepository extends Repository<Airline, EntityId> {
    List<Airline> findAllAirlines();
}
