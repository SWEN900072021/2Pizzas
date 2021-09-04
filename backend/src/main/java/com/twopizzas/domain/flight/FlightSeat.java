package com.twopizzas.domain.flight;

import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.util.AssertionConcern;

import java.util.Objects;

public class FlightSeat extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final String name;
    private final SeatClass seatClass;
    private final ValueHolder<Flight> flight;

    public FlightSeat(EntityId id, String name, SeatClass seatClass, ValueHolder<Flight> flight) {
        this.id = notNull(id, "id");
        this.name = notNullAndNotBlank(name, "name");
        this.seatClass = notNull(seatClass, "seatClass");
        this.flight = notNull(flight, "flight");
    }

    public FlightSeat(String name, SeatClass seatClass, Flight flight) {
        this(EntityId.nextId(), name, seatClass, () -> flight);
    }

    public String getName() {
        return name;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public Flight getFlight() {
        return flight.get();
    }

    @Override
    public EntityId getId() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightSeat that = (FlightSeat) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
