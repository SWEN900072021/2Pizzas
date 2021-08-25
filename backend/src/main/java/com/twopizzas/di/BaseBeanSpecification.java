package com.twopizzas.di;


import com.twopizzas.util.AssertionConcern;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class BaseBeanSpecification<T> extends AssertionConcern implements ComponentSpecification<T> {
    private final Class<T> clasz;

    BaseBeanSpecification(Class<T> clasz) {
        this.clasz = notNull(clasz, "clasz");
    }

    @Override
    public String describe() {
        return String.format("of type %s", clasz.getName());
    }

    @Override
    public Class<T> getClasz() {
        return clasz;
    }

    @Override
    public Collection<Bean<T>> filter(Collection<Bean<?>> beans) {
        Set<Bean<T>> filtered = new HashSet<>();
        for (Bean<?> bean : beans) {
            if (clasz.isAssignableFrom(bean.getClasz())) {
                filtered.add(unsafeCast(bean));
            }
        }
        return filtered;
    }

    @SuppressWarnings({"unchecked"})
    private <U> U unsafeCast(Object o) {
        return (U) o;
    }
}
