package com.twopizzas.data;

public interface ValueHolder<T> {
    T get();
    boolean isPresent();
}
