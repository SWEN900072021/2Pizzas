package com.twopizzas.domain.flight;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.airport.Airport;
import com.twopizzas.domain.booking.TimePeriod;
import com.twopizzas.util.AssertionConcern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FlightSearch extends AssertionConcern {
    private final TimePeriod departing;
    private final EntityId from;
    private final EntityId to;
    private final EntityId airline;

    @Builder
    private FlightSearch(TimePeriod departing, EntityId from, EntityId to, EntityId airline) {
        this.departing = departing;
        this.from = from;
        this.to = to;
        this.airline = airline;
    }
}
