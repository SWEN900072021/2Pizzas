package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.List;

public interface FlightRepository extends Repository<Flight, EntityId> {
    List<Flight> customerFlights(EntityId customerId);
    List<Flight> searchFlights(FlightSearch search);
}
