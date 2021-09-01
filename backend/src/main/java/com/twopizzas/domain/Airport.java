package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Airport extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private String code;
    private String name;
    private String location;
    private ZoneOffset utcOffset;

    public Airport(EntityId id, String code, String name, String location, ZoneOffset utcOffset) {
        this.id = notNull(id, "id");
        this.code = notNullAndNotBlank(code, "code");
        this.name = notNullAndNotBlank(name, "name");
        this.location = notNullAndNotBlank(location, "location");
        this.utcOffset = notNull(utcOffset, "utcOffset");
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    public String getLocation() { return location; }

    public ZoneOffset getUtcOffset() { return utcOffset; }
}
