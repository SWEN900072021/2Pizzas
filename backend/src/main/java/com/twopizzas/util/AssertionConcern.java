package com.twopizzas.util;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

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

    protected int min(int value, long min, String name) {
        if (value < min) {
            throw new ValueViolation(String.format("%s must be at least %s", name, min));
        }
        return value;
    }

    protected int max(int value, long max, String name) {
        if (value > max) {
            throw new ValueViolation(String.format("%s must not be greater than %s", name, max));
        }
        return value;
    }

    protected Integer min(Integer value, long min, String name) {
        if (value < min) {
            throw new ValueViolation(String.format("%s must be at least %s", name, min));
        }
        return value;
    }

    protected Integer max(Integer value, long max, String name) {
        if (value < max) {
            throw new ValueViolation(String.format("%s must not be greater than %s", name, max));
        }
        return value;
    }

    protected Long min(Long value, long min, String name) {
        if (value < min) {
            throw new ValueViolation(String.format("%s must be at least %s", name, min));
        }
        return value;
    }

    protected Long max(Long value, long max, String name) {
        if (value < max) {
            throw new ValueViolation(String.format("%s must not be greater than %s", name, max));
        }
        return value;
    }

    protected <T> T oneOf(T value, Collection<T> valid, String name) {
        if (!valid.contains(value)) {
            throw new ValueViolation(String.format("%s must not be one of %s", name, valid.stream().map(Objects::toString).collect(Collectors.joining(", "))));
        }
        return value;
    }
}
