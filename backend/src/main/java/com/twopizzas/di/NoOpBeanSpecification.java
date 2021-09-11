package com.twopizzas.di;

import java.util.Collection;

public class NoOpBeanSpecification<T> implements TypedComponentSpecification<T> {

    private final TypedComponentSpecification<T> wrapped;

    public NoOpBeanSpecification(TypedComponentSpecification<T> wrapped) {
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
