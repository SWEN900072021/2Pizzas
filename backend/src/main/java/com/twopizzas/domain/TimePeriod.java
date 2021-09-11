package com.twopizzas.domain;

import com.twopizzas.util.ValueViolation;

import java.time.OffsetDateTime;

public class TimePeriod {
    private final OffsetDateTime start;
    private final OffsetDateTime end;

    public TimePeriod(OffsetDateTime start, OffsetDateTime end) {
        if (start.isAfter(end)) {
            throw new ValueViolation("time period is invalid, end must not occur before start");
        }
        this.start = start;
        this.end = end;
    }

    public OffsetDateTime getStart() {
        return start;
    }

    public OffsetDateTime getEnd() {
        return end;
    }
}
