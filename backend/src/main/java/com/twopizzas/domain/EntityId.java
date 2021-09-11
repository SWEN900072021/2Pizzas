package com.twopizzas.domain;

import com.twopizzas.util.AssertionConcern;

import java.util.Objects;
import java.util.UUID;

public class EntityId extends AssertionConcern {
    private final UUID value;

    private EntityId(UUID value) {
        notNull(value, "value");
        this.value = value;
    }

    public static EntityId of(UUID value) {
        return new EntityId(value);
    }

    public static EntityId of(String value) {
        return new EntityId(UUID.fromString(value));
    }

    public static EntityId nextId() {
        return new EntityId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId entityId = (EntityId) o;
        return value.equals(entityId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
