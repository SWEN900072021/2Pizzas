package com.twopizzas.di;

interface ComponentConstructor<T> {
    T construct(ComponentManager componentManager);
}
