package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.util.AssertionConcern;

import java.util.Date;
import java.util.List;

public class Booking extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;

    private Date date;
    private Double totalCost;
    private String reference;

    private ValueHolder<List<Passenger>> passengers;

    private Flight flightId;

    Booking(EntityId id) {
        notNull(id, "id");
        this.id = id;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public List<Passenger> getPassengers() {
        return passengers.get();
    }

    public Date getDate() {
        return date;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public String getBookingReference() {
        return reference;
    }
}
