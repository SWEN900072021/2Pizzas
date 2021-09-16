package com.twopizzas.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

class ThreadLocalBeanProxy<T> implements Bean<T> {
    private final Bean<T> wrapped;

    ThreadLocalBeanProxy(Bean<T> wrapped) {
        this.wrapped = wrapped;
    }

    // for testing only
    Bean<T> getWrapped() {
        return wrapped;
    }

    @Override
    public T construct(ComponentManager componentManager) {
        // wrap in thread local proxy
        ThreadLocalProxy<T> proxy = new ThreadLocalProxy<>(wrapped, componentManager);
        return (T) Proxy.newProxyInstance(getClasz().getClassLoader(), getClasz().getInterfaces(), proxy);
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
