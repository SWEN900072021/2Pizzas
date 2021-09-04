package com.twopizzas.domain.flight;

import com.twopizzas.data.Entity;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.util.AssertionConcern;

public class FlightSeatAllocation extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;
    private final FlightSeat seat;
    private final Passenger passenger;

    public FlightSeatAllocation(EntityId id, FlightSeat seat, Passenger passenger) {
        this.id = id;
        this.seat = seat;
        this.passenger = passenger;
    }

    public FlightSeatAllocation(FlightSeat seat, Passenger passenger) {
        this(EntityId.nextId(), seat, passenger);
    }

    public FlightSeat getSeat() {
        return seat;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    @Override
    public EntityId getId() {
        return id;
    }
}
