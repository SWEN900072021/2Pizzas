package com.twopizzas.domain.flight;

import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.TimePeriod;
import com.twopizzas.util.AssertionConcern;
import com.twopizzas.util.ValueViolation;

import java.time.OffsetDateTime;

public class StopOver extends AssertionConcern {
    private final Airport location;
    private final OffsetDateTime arrival;
    private final OffsetDateTime departure;
    private final ValueHolder<Flight> flight;

    public StopOver(Airport location, OffsetDateTime arrival, OffsetDateTime departure, ValueHolder<Flight> flight) {
        this.location = notNull(location, "location");
        this.arrival = notNull(arrival, "arrival");
        this.departure = notNull(departure, "departure");
        this.flight = notNull(flight, "flight");

        if (departure.isBefore(arrival)) {
            throw new ValueViolation("departure time must be after arrival time");
        }
    }

    public Flight getFlight() {
        return flight.get();
    }

    public Airport getLocation() {
        return location;
    }

    public OffsetDateTime getDeparture() {
        return departure;
    }

    public OffsetDateTime getArrival() {
        return arrival;
    }
}
