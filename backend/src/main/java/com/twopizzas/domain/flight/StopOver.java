package com.twopizzas.domain.flight;

import com.twopizzas.domain.Airport;
import com.twopizzas.util.AssertionConcern;
import com.twopizzas.util.ValueViolation;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
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
}
