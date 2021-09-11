package com.twopizzas.di;

public class LoadBeanException extends ApplicationContextException {
    private static final String MESSAGE_TEMPLATE = "failed to load bean for component %s: %s";
    LoadBeanException(Class<?> beanClasz, String message) {
        super(String.format(MESSAGE_TEMPLATE, beanClasz.getName(), message));
    }

    LoadBeanException(Class<?> beanClasz, Throwable cause) {
        super(String.format(MESSAGE_TEMPLATE, beanClasz.getName(), cause.getMessage()));
    }
}
