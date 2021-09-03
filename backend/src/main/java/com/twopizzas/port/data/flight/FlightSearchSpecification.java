package com.twopizzas.port.data.flight;

import com.twopizzas.domain.Flight;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class FlightSearchSpecification implements FlightSpecification {


    @Override
    public List<Flight> execute(ConnectionPool context) {
        return null;
    }
}
