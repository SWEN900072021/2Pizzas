package com.twopizzas.domain.flight;

import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.booking.TimePeriod;
import com.twopizzas.util.AssertionConcern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FlightSearch extends AssertionConcern {
    private final TimePeriod departing;
    private final TimePeriod returning;
    private final Airport from;
    private final Airport to;
    private final Airline airline;
    private final int passengers;

    @Builder
    private FlightSearch(TimePeriod departing, TimePeriod returning, Airport from, Airport to, Airline airline, int passengers) {
        this.departing = departing;
        this.returning = returning;
        this.from = from;
        this.to = to;
        this.airline = airline;
        this.passengers = passengers;
    }
}
