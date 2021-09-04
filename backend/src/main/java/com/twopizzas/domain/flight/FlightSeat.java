package com.twopizzas.domain.flight;

import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.util.AssertionConcern;

import java.util.Optional;

public class FlightSeat extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final String name;
    private final SeatClass seatClass;

    public FlightSeat(EntityId id, String name, SeatClass seatClass) {
        this.id = notNull(id, "id");
        this.name = notNullAndNotBlank(name, "name");
        this.seatClass = notNull(seatClass, "seatClass");
    }

    public FlightSeat(String name, SeatClass seatClass) {
        this(EntityId.nextId(), name, seatClass);
    }

    public String getName() {
        return name;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }
}
