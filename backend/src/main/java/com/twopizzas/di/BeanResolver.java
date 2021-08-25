package com.twopizzas.di;

import java.util.Collection;

interface BeanResolver {
    <T> Collection<Bean<T>> resolve(ComponentSpecification<T> specification, Collection<Bean<?>> beans);
}
