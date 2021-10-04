package com.twopizzas.domain.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;

@Getter
public class FlightSeat extends DomainEntity {

    private final String name;
    private final SeatClass seatClass;
    private final ValueHolder<Flight> flight;

    public FlightSeat(EntityId id, String name, SeatClass seatClass, ValueHolder<Flight> flight) {
        super(id);
        this.name = notNullAndNotBlank(name, "name");
        this.seatClass = notNull(seatClass, "seatClass");
        this.flight = notNull(flight, "flight");
    }

    public FlightSeat(String name, SeatClass seatClass, Flight flight) {
        this(EntityId.nextId(), name, seatClass, BaseValueHolder.of(flight));
    }

    public Flight getFlight() {
        return flight.get();
    }
}
