package com.twopizzas.di;

import java.util.Collection;

interface BeanResolver {
    <T> Collection<Bean<T>> resolve(TypedComponentSpecification<T> specification, Collection<Bean<?>> beans);
}
