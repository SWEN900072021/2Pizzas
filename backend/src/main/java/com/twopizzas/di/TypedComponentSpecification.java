package com.twopizzas.di;

import java.util.Collection;

interface TypedComponentSpecification<T> {
    String describe();
    Class<T> getClasz();
    Collection<Bean<T>> filter(Collection<Bean<?>> beans);
}
