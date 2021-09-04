package com.twopizzas.domain.flight;

import java.util.List;

public class SeatBooking {
    private final List<FlightSeatAllocation> allocations;
    private final Flight flight;

    public SeatBooking(List<FlightSeatAllocation> allocations, Flight flight) {
        this.allocations = allocations;
        this.flight = flight;
    }
}
