package com.twopizzas.port.data.airplane;

import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AllAirplaneProfilesSpecification implements AirplaneProfileSpecification {
    @Override
    public List<AirplaneProfile> execute(ConnectionPool context) {
        return null;
    }
}
