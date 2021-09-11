package com.twopizzas.di;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ComponentManager {

    private final Collection<Bean<?>> store = new ArrayList<>();;
    private final BeanResolver beanResolver;
    private final BeanLoader beanLoader;

    ComponentManager(BeanResolver beanResolver, BeanLoader beanLoader) {
        this.beanResolver = beanResolver;
        this.beanLoader = beanLoader;
    }

    // for testing only
    BeanResolver getBeanResolver() {
        return beanResolver;
    }

    void init() {
        store.addAll(beanLoader.load());
    }

    <T> T getComponent(ComponentSpecification<T> specification) throws ApplicationContextException {
        return getBean(specification).construct(this);
    }

    public <T> T getComponent(Class<T> clasz) throws ApplicationContextException {
        return getBean(new BaseBeanSpecification<>(clasz)).construct(this);
    }

    <T> Bean<T> getBean(ComponentSpecification<T> specification) {
        Collection<Bean<T>> beans = beanResolver.resolve(specification, store);

        if (beans.size() > 1) {
            throw new DuplicateComponentException(String.format("multiple components found for component specification %s, expected only one component but found %s",
                    specification.describe(),
                    beans.stream().map(Bean::getClasz).map(Class::getName).collect(Collectors.joining(", "))
            ));
        }

        if (beans.size() == 1) {
            return beans.iterator().next();
        }

        throw new ComponentNotFound(specification.getClass());
    }
}
