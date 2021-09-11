package com.twopizzas.di;

import java.util.Collection;
import java.util.stream.Collectors;

public class PrimaryBeanResolver implements BeanResolver {
    private final BeanResolver wrapped;

    public PrimaryBeanResolver(BeanResolver wrapped) {
        this.wrapped = wrapped;
    }

    // for testing only
    BeanResolver getWrapped() {
        return wrapped;
    }

    @Override
    public <T> Collection<Bean<T>> resolve(TypedComponentSpecification<T> specification, Collection<Bean<?>> beans) {
        Collection<Bean<T>> resolved = wrapped.resolve(specification, beans);
        Collection<Bean<T>> primary = resolved.stream().filter(Bean::isPrimary).collect(Collectors.toSet());
        if (primary.isEmpty()) {
            return resolved;
        }
        return primary;
    }
}
