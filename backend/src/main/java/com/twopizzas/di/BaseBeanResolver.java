package com.twopizzas.di;

import java.util.Collection;

public class BaseBeanResolver implements BeanResolver {
    @Override
    public <T> Collection<Bean<T>> resolve(ComponentSpecification<T> specification, Collection<Bean<?>> beans) {
        return specification.filter(beans);
    }
}
