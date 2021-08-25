package com.twopizzas.di;

import com.twopizzas.util.AssertionConcern;

import java.util.Collection;
import java.util.stream.Collectors;

class QualifiedBeanSpecification<T> extends AssertionConcern implements ComponentSpecification<T> {

    private final ComponentSpecification<T> wrapped;
    private final String qualifier;

    public QualifiedBeanSpecification(String qualifier, ComponentSpecification<T> wrapped) {
        this.wrapped = wrapped;
        this.qualifier = qualifier;
    }

    String getQualifier() { return qualifier; }

    @Override
    public String describe() {
        return String.format("%s and qualifier %s", wrapped.describe(), qualifier);
    }

    @Override
    public Class<T> getClasz() {
        return wrapped.getClasz();
    }

    @Override
    public Collection<Bean<T>> filter(Collection<Bean<?>> beans) {
        return wrapped.filter(beans).stream()
                .filter(b -> qualifier.equals(b.getQualifier()))
                .collect(Collectors.toSet());
    }
}
