package com.twopizzas.domain;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.util.AssertionConcern;

import java.time.OffsetDateTime;
import java.util.List;

public class Flight extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final ValueHolder<List<Booking>> bookings;
    private final AirplaneProfile airplaneProfile;
    private final Airline airline;
    private final ValueHolder<List<Seat>> seats;
    private final Airport origin;
    private final Airport destination;
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    private final List<StopOver> stopOvers;
    private final String code;
    private Status status;

    public Flight(EntityId id, ValueHolder<List<Booking>> bookings, AirplaneProfile airplaneProfile, Airline airline, ValueHolder<List<Seat>> seats, Airport origin, Airport destination, List<StopOver> stopOvers, String code) {
        this.id = notNull(id, "id");
        this.bookings = bookings;
        this.airplaneProfile = airplaneProfile;
        this.airline = airline;
        this.seats = seats;
        this.origin = origin;
        this.destination = destination;
        this.stopOvers = stopOvers;
        this.code = code;
    }

    public Flight(AirplaneProfile airplaneProfile, Airline airline, Airport origin, Airport destination, List<StopOver> stopOvers, String code) {
        this(EntityId.nextId(), new BaseValueHolder<>(null), airplaneProfile, airline, new BaseValueHolder<>(null), origin, destination, stopOvers, code);
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public enum Status {
        CANCELLED,
        TO_SCHEDULE,
        DELAYED,
    }
}
