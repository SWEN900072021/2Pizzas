package com.twopizzas.util;

import java.util.Collection;

public class AssertionConcern {

    protected void notNull(Object value, String name) {
        if (value == null) {
            throw new ValueViolation(String.format("%s must not be null", name));
        }
    }

    protected void notBlank(String value, String name) {
        if (value != null && value.trim().equals("")) {
            throw new ValueViolation(String.format("%s must not be blank", name));
        }
    }

    protected  void notEmpty(Collection<?> value, String name) {
        if (value != null && value.isEmpty()) {
            throw new ValueViolation(String.format("%s must not be empty", name));
        }
    }
}
