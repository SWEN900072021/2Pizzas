package com.twopizzas.port.data.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.util.AssertionConcern;

public class FlightByIdLoader extends AssertionConcern implements ValueLoader<Flight> {

    private final FlightMapper mapper;
    private final EntityId flightId;

    public FlightByIdLoader(FlightMapper mapper, EntityId flightId) {
        this.mapper = notNull(mapper, "mapper");
        this.flightId = notNull(flightId, "flightId");
    }

    public FlightByIdLoader(FlightMapper mapper, String flightId) {
        this(mapper, EntityId.of(flightId));
    }

    @Override
    public ValueHolder<Flight> load() {
        return new BaseValueHolder<>(mapper.read(flightId));
    }
}
