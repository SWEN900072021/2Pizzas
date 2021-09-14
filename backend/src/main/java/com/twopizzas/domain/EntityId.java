package com.twopizzas.domain;

import com.twopizzas.util.AssertionConcern;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class EntityId extends AssertionConcern {

    @EqualsAndHashCode.Include
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

}
