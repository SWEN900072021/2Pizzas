package com.twopizzas.domain.flight;

import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.util.AssertionConcern;

import java.math.BigDecimal;

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

    public BigDecimal getCost() {
        switch (seat.getSeatClass()) {
            case FIRST: return seat.getFlight().getFirstClassCost();
            case BUSINESS: return seat.getFlight().getBusinessClassCost();
            case ECONOMY: return seat.getFlight().getEconomyClassCost();
            default: throw new IllegalStateException(String.format("error calculating cost of unknown seatClass %s", seat.getSeatClass()));
        }
    }
}
