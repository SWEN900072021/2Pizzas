package com.twopizzas.util;

public class ValueViolation extends RuntimeException {
    ValueViolation(String message) {
        super(message);
    }
}
