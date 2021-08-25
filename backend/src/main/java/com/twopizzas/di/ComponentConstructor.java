package com.twopizzas.di;

import java.util.List;

interface ComponentConstructor<T> {

    T construct(ComponentManager componentManager);
}
