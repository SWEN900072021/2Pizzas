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

    public StopOver(Airport location, OffsetDateTime arrival, OffsetDateTime departure) {
        this.location = notNull(location, "location");
        this.arrival = notNull(arrival, "arrival");
        this.departure = notNull(departure, "departure");

        if (departure.isBefore(arrival)) {
            throw new ValueViolation("departure time must be after arrival time");
        }
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
