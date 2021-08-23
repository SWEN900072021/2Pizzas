package com.twopizzas.di;

public class ComponentNotFound extends ApplicationContextException {
    ComponentNotFound(Class<?> clasz) {
        super(String.format("%s is not a managed component, application context only manages classes decorated with %s", clasz.getName(), Component.class.getName()));
    }
    ComponentNotFound(String message) {
        super(message);
    }
}
