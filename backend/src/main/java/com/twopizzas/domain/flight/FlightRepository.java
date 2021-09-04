package com.twopizzas.domain.flight;

import com.twopizzas.data.Repository;
import com.twopizzas.domain.EntityId;

import java.util.List;

public interface FlightRepository extends Repository<Flight, EntityId> {
    List<Flight> customerFlights(EntityId customerId);
    List<Flight> searchFlights(FlightSearch search);
}
