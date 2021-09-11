package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.util.AssertionConcern;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Booking extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final OffsetDateTime date;
    private BigDecimal totalCost;
    private final Customer customer;

    private SeatBooking flightBooking = null;
    private SeatBooking returnFlightBooking = null;

    public Booking(EntityId id, OffsetDateTime date, BigDecimal totalCost, Customer customer) {
        this.id = notNull(id, "id");
        this.date = notNull(date, "date");
        this.totalCost = notNull(totalCost, "totalCost");
        this.customer = notNull(customer, "customer");
    }

    public Booking(Customer customer) {
        this(EntityId.nextId(), OffsetDateTime.now(), null, customer);
    }


    public void addFlight(SeatBooking seatBooking) {
        flightBooking = seatBooking;
    }

    public void addReturnFlight(SeatBooking seatBooking) {
        returnFlightBooking = seatBooking;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public Customer getCustomer() { return customer; }

    public SeatBooking getFlightReservation() {
        return flightBooking;
    }

    public SeatBooking getReturnFlightReservation() {
        return returnFlightBooking;
    }
}
