package com.twopizzas.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

class SingletonBeanProxy<T> implements Bean<T> {
    private final Bean<T> wrapped;
    private T instance;

    SingletonBeanProxy(Bean<T> wrapped) {
        this.wrapped = wrapped;
    }

    // for testing only
    Bean<T> getWrapped() {
        return wrapped;
    }

    @Override
    public T construct(ComponentManager componentManager) {
        if (instance == null) {
            instance = wrapped.construct(componentManager);
        }
        return instance;
    }

    @Override
    public Class<T> getClasz() {
        return wrapped.getClasz();
    }

    @Override
    public String getQualifier() {
        return wrapped.getQualifier();
    }

    @Override
    public List<String> getProfiles() {
        return wrapped.getProfiles();
    }

    @Override
    public Constructor<T> getConstructor() {
        return wrapped.getConstructor();
    }

    @Override
    public Method getPostConstruct() {
        return wrapped.getPostConstruct();
    }

    @Override
    public boolean isPrimary() {
        return wrapped.isPrimary();
    }

    @Override
    public List<TypedComponentSpecification<?>> getDependencies() {
        return wrapped.getDependencies();
    }
}
