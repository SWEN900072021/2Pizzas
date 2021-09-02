package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

import java.time.ZoneId;

public class Airport extends AssertionConcern implements Entity<EntityId> {

    private static final int MAX_AIRPORT_CODE_LENGTH = 3;

    private final EntityId id;
    private final String code;
    private final String name;
    private final String location;
    private final ZoneId utcOffset;

    public Airport(EntityId id, String code, String name, String location, ZoneId utcOffset) {
        this.id = notNull(id, "id");
        this.code = max(notNullAndNotBlank(code, "code"), MAX_AIRPORT_CODE_LENGTH, "code");
        this.name = notNullAndNotBlank(name, "name");
        this.location = notNullAndNotBlank(location, "location");
        this.utcOffset = notNull(utcOffset, "utcOffset");
    }

    public Airport(String code, String name, String location, ZoneId utcOffset) {
        this(EntityId.nextId(), code, name, location, utcOffset);
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    public String getLocation() { return location; }

    public ZoneId getUtcOffset() { return utcOffset; }
}
