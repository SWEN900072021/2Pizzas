package com.twopizzas.domain.flight;

import com.twopizzas.domain.Airport;
import com.twopizzas.domain.TimePeriod;

public class StopOver {
    private final Airport location;
    private final TimePeriod duration;

    public StopOver(Airport location, TimePeriod duration) {
        this.location = location;
        this.duration = duration;
    }
}
