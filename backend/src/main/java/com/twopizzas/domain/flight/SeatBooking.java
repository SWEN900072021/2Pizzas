package com.twopizzas.domain.flight;

import java.util.Set;

public class SeatBooking {
    private final Set<FlightSeatAllocation> allocations;
    private final Flight flight;

    public SeatBooking(Flight flight, Set<FlightSeatAllocation> allocations) {
        this.allocations = allocations;
        this.flight = flight;
    }

    public Set<FlightSeatAllocation> getAllocations() {
        return allocations;
    }

    public Flight getFlight() {
        return flight;
    }
}
