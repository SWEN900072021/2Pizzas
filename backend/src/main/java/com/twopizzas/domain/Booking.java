package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.util.AssertionConcern;

import java.time.LocalDateTime;

public class Booking extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final LocalDateTime date;
    private final Double totalCost;
    private final String reference;

    private SeatBooking flightBooking;
    private SeatBooking returnFlightBooking;

    public Booking(EntityId id, LocalDateTime date, Double totalCost, String reference) {

        this.id = notNull(id, "id");
        this.date = notNull(date, "date");
        this.totalCost = notNull(totalCost, "totalCost");
        this.reference = notNullAndNotBlank(reference, "reference");

    }

    @Override
    public EntityId getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public String getBookingReference() {
        return reference;
    }
}
