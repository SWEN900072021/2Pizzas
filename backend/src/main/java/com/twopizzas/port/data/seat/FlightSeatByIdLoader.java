package com.twopizzas.port.data.seat;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.util.AssertionConcern;

public class FlightSeatByIdLoader extends AssertionConcern implements ValueLoader<FlightSeat> {

    private final FlightSeatMapper mapper;
    private final EntityId seatId;

    public FlightSeatByIdLoader(FlightSeatMapper mapper, EntityId seatId) {
        this.mapper = notNull(mapper, "mapper");
        this.seatId = notNull(seatId, "seatId");
    }

    public FlightSeatByIdLoader(FlightSeatMapper mapper, String seatId) {
        this(mapper, EntityId.of(seatId));
    }

    @Override
    public ValueHolder<FlightSeat> load() {
        return BaseValueHolder.of(mapper.read(seatId));
    }
}
