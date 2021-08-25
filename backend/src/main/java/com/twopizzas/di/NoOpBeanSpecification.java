package com.twopizzas.di;

import java.util.Collection;

public class NoOpBeanSpecification<T> implements ComponentSpecification<T> {

    private final ComponentSpecification<T> wrapped;

    public NoOpBeanSpecification(ComponentSpecification<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String describe() {
        return "";
    }

    @Override
    public Class<T> getClasz() {
        return wrapped.getClasz();
    }

    @Override
    public Collection<Bean<T>> filter(Collection<Bean<?>> beans) {
        return wrapped.filter(beans);
    }
}
