package com.twopizzas.di;

public class DuplicateComponentException extends RuntimeException {
    public DuplicateComponentException(String message) {
        super(message);
    }
}
