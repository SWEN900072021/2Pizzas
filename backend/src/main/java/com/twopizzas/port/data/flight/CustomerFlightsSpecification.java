package com.twopizzas.port.data.flight;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class CustomerFlightsSpecification implements FlightSpecification {
    private static final String template =
            "SELECT * from WHERE ";

    private final EntityId customerId;

    public CustomerFlightsSpecification(EntityId customerId) {
        this.customerId = customerId;
    }

    @Override
    public List<Flight> execute(ConnectionPool context) {
        return null;
    }
}
