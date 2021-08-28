package com.twopizzas.data;

public interface Entity<T> {
    T getId();
    default boolean isNew() {
        return true;
    }
}
