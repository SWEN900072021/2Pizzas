package com.twopizzas.domain.flight;

import com.twopizzas.domain.Passenger;
import com.twopizzas.util.AssertionConcern;

public class FlightSeatAllocation extends AssertionConcern {
    private final FlightSeat seat;
    private final Passenger passenger;

    public FlightSeatAllocation(FlightSeat seat, Passenger passenger) {
        this.seat = notNull(seat, "seat");
        this.passenger = notNull(passenger, "passenger");
    }

    public FlightSeat getSeat() {
        return seat;
    }

    public Passenger getPassenger() {
        return passenger;
    }


}
