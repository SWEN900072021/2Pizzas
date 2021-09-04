package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.util.AssertionConcern;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Booking extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final OffsetDateTime date;
    private Double totalCost;
    private final String reference;
    private final Customer customer;

    private SeatBooking flightBooking;
    private SeatBooking returnFlightBooking;

    public Booking(EntityId id, OffsetDateTime date, Double totalCost, String reference, Customer customer) {
        this.id = notNull(id, "id");
        this.date = notNull(date, "date");
        this.totalCost = notNull(totalCost, "totalCost");
        this.reference = notNullAndNotBlank(reference, "reference");
        this.customer = notNull(customer, "customer");
    }

    public Booking(Customer customer) {
        this(EntityId.nextId(), OffsetDateTime.now(), null, nextReference(), customer);
    }

    public static String nextReference() {
        return "something";
    }

    public void addFlight(SeatBooking seatBooking) {
        updateAllocations(seatBooking);
        flightBooking = seatBooking;
    }

    public void addReturnFlight(SeatBooking seatBooking) {
        updateAllocations(seatBooking);
        returnFlightBooking = seatBooking;
    }

    private void updateAllocations(SeatBooking booking) {
        booking.getAllocations().forEach(b -> b.setBooking(this));
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public String getBookingReference() {
        return reference;
    }
}
