package com.twopizzas.util;

import java.util.Collection;

public abstract class AssertionConcern {

    protected <T> T notNull(T value, String name) {
        if (value == null) {
            throw new ValueViolation(String.format("%s must not be null", name));
        }
        return value;
    }

    protected String notBlank(String value, String name) {
        if (value != null && value.trim().equals("")) {
            throw new ValueViolation(String.format("%s must not be blank", name));
        }
        return value;
    }

    protected <U, T extends Collection<U>> T notEmpty(T value, String name) {
        if (value != null && value.isEmpty()) {
            throw new ValueViolation(String.format("%s must not be empty", name));
        }
        return value;
    }
}
