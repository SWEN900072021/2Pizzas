package com.twopizzas.data;

public interface ValueLoader<T> {
    ValueHolder<T> load();
}
