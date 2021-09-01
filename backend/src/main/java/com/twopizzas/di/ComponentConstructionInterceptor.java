package com.twopizzas.di;

public interface ComponentConstructionInterceptor {
    <T> T intercept(T component, ComponentManager componentManager);
}
