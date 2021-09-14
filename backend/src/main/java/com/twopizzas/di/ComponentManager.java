package com.twopizzas.di;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentManager {

    private final Collection<Bean<?>> store = new ArrayList<>();;
    private final BeanResolver beanResolver;
    private final BeanLoader beanLoader;
    private ApplicationContext context;

    ComponentManager(BeanResolver beanResolver, BeanLoader beanLoader) {
        this.beanResolver = beanResolver;
        this.beanLoader = beanLoader;
    }

    // for testing only
    BeanResolver getBeanResolver() {
        return beanResolver;
    }

    void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    void init() {
        if (context == null) {
            throw new ApplicationContextException("init called on componentManager with null application context");
        }
        store.addAll(beanLoader.load());
        store.add(new ApplicationContextBean(context));
        store.add(new ConfigurationContextBean(context.getProfile()));
        store.forEach(b -> b.construct(this));
    }

    <T> T getComponent(TypedComponentSpecification<T> specification) throws ApplicationContextException {
        return getBean(specification).construct(this);
    }

    Collection<?> getComponents(ComponentSpecification specification) throws ApplicationContextException {
        return specification.filter(store).stream().map(b -> b.construct(this)).collect(Collectors.toList());
    }

    public <T> T getComponent(Class<T> clasz) throws ApplicationContextException {
        return getBean(new BaseBeanSpecification<>(clasz)).construct(this);
    }

    <T> Bean<T> getBean(TypedComponentSpecification<T> specification) {
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

        throw new ComponentNotFound(specification.getClasz());
    }
}
