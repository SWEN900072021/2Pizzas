package com.twopizzas.di;

public class ComponentInstantiationError extends ApplicationContextException {
    ComponentInstantiationError(String message) {
        super(message);
    }

    ComponentInstantiationError(String message, Throwable cause) {
        super(message, cause);
    }
}
