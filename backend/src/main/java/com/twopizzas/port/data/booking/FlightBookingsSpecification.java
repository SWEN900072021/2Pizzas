package com.twopizzas.port.data.booking;

import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;

import java.util.List;

public class FlightBookingsSpecification implements BookingSpecification {
    private final EntityId flightId;

    public FlightBookingsSpecification(EntityId flightId) {
        this.flightId = flightId;
    }

    @Override
    public List<Booking> execute() {
        return null;
    }
}
