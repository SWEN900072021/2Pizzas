package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.util.AssertionConcern;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Booking extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;

    private Date date;
    private Time time;
    private String totalCost;
    private String bookingReference;
    private ValueHolder<List<Passenger>> passengers;
    private Flight flight;

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

    public Time getTime() {
        return time;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public String getBookingReference() {
        return bookingReference;
    }
}
