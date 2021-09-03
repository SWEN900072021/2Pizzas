package com.twopizzas.domain;

public class StopOver {
    private final Airport location;
    private final TimePeriod duration;

    public StopOver(Airport location, TimePeriod duration) {
        this.location = location;
        this.duration = duration;
    }
}
