package com.twopizzas.domain.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.domain.error.BusinessRuleException;
import com.twopizzas.util.AssertionConcern;

public class FlightSeatAllocation extends AssertionConcern {
    private final FlightSeat seat;
    private final Passenger passenger;
    private ValueHolder<Booking> booking;

    public FlightSeatAllocation(FlightSeat seat, Passenger passenger, ValueHolder<Booking> booking) {
        this.seat = notNull(seat, "seat");
        this.passenger = notNull(passenger, "passenger");
        this.booking = booking;
    }

    public FlightSeatAllocation(FlightSeat seat, Passenger passenger) {
        this(seat, passenger, () -> null);
    }

    public FlightSeat getSeat() {
        return seat;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setBooking(Booking booking) {
        if (this.booking.get() != null) {
            throw new BusinessRuleException("");
        }
        this.booking = new BaseValueHolder<>(booking);
    }

    public Booking getBooking() {
        return booking.get();
    }
}
