package com.twopizzas.di;

import java.util.List;

public interface ComponentFactory {
    <T> T make(Bean<T> bean, List<Object> args);
}
