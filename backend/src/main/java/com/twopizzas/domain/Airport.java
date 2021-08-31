package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

public class Airport extends AssertionConcern implements Entity<EntityId> {
    private final Entity id;

    private String code;
    private String name;
    private String location;
    private float utcOffset;

    Airport(EntityId id) {
        notNull(id, "id");
        this.id = id;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    public String getLocation() { return location; }

    public float getUtcOffset() { return utcOffset; }

}
