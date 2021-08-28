package com.twopizzas.di;

public interface ComponentConstructionInterceptor<T> {
    Class<T> interceptComponentsOfClass();
    T intercept(T component);
}
