package com.twopizzas.domain.error;

import com.twopizzas.domain.EntityId;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String name, EntityId id) {
        this(name, id.toString());
    }

    public NotFoundException(String name, String id) {
        super(String.format("no %s with id %s", name, id));
    }
}
