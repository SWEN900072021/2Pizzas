package com.twopizzas.domain.flight;

import com.twopizzas.domain.Passenger;

public class FlightSeatAllocation {
    private final FlightSeat seat;
    private final Passenger passenger;

    public FlightSeatAllocation(FlightSeat seat, Passenger passenger) {
        this.seat = seat;
        this.passenger = passenger;
    }

    public FlightSeat getSeat() {
        return seat;
    }

    public Passenger getPassenger() {
        return passenger;
    }
}
