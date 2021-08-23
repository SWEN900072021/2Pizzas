package com.twopizzas.di;

public class ComponentInstantiationException extends ApplicationContextException {
    ComponentInstantiationException(String message) {
        super(message);
    }

    ComponentInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
