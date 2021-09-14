package com.twopizzas.domain.user;

import com.twopizzas.data.Repository;
import com.twopizzas.domain.EntityId;

import java.util.List;

public interface AirlineRepository extends Repository<Airline, EntityId> {
    List<Airline> findAllAirlines();
}
