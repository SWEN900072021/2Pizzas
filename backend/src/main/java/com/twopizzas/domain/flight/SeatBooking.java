package com.twopizzas.domain.flight;

import lombok.Getter;

import java.util.Set;

@Getter
public class SeatBooking {
    private final Set<FlightSeatAllocation> allocations;
    private final Flight flight;

    public SeatBooking(Flight flight, Set<FlightSeatAllocation> allocations) {
        this.allocations = allocations;
        this.flight = flight;
    }
}
