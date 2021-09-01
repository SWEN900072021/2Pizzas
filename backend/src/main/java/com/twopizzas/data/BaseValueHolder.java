package com.twopizzas.data;

public class BaseValueHolder<T> implements ValueHolder<T> {

    private final T value;

    public BaseValueHolder(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }
}
