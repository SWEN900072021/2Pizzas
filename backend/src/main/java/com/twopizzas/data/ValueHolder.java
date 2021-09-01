package com.twopizzas.data;

@FunctionalInterface
public interface ValueHolder<T> {
    T get();
    default boolean isPresent() {
        return get() != null;
    };
}
