package com.twopizzas.di;

import com.twopizzas.util.AssertionConcern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

class BaseBean<T> extends AssertionConcern implements Bean<T> {

    private final Constructor<T> constructor;
    private final List<TypedComponentSpecification<?>> dependencies;
    private final Method postConstruct;
    private final Class<T> clasz;
    private final String qualifier;
    private final List<String> profiles;
    private final boolean primary;
    private final List<ComponentConstructionInterceptor> interceptors;

    BaseBean(Class<T> clasz,
             String qualifier,
             List<String> profiles,
             boolean primary,
             Constructor<T> constructor,
             List<TypedComponentSpecification<?>> dependencies,
             Method postConstruct,
             List<ComponentConstructionInterceptor> interceptors)
    {
        this.clasz = clasz;
        this.qualifier = qualifier;
        this.profiles = profiles;
        this.constructor = constructor;
        this.dependencies = dependencies;
        this.postConstruct = postConstruct;
        this.primary = primary;
        this.interceptors = interceptors;
    }

    @Override
    public Class<T> getClasz() {
        return clasz;
    }

    @Override
    public String getQualifier() {
        return qualifier;
    }

    @Override
    public List<String> getProfiles() {
        return profiles;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Method getPostConstruct() {
        return postConstruct;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    @Override
    public List<TypedComponentSpecification<?>> getDependencies() { return dependencies; }

    @Override
    public T construct(ComponentManager componentManager) {
        List<Object> dependencyInstances = dependencies.stream()
                .map(componentSpecification -> {
                    // lazy load deps to avoid cycles
                    Bean<?> bean = componentManager.getBean(componentSpecification);
                    LazyComponentProxy<?> lazyComponentProxy = new LazyComponentProxy<>(componentManager, bean);
                    Object proxy = Proxy.newProxyInstance(bean.getClasz().getClassLoader(),
                            new Class[]{componentSpecification.getClasz()}, lazyComponentProxy);
                    return componentSpecification.getClasz().cast(proxy);
                }).collect(Collectors.toList());
        return build(dependencyInstances, componentManager);
    }

    protected T build(List<Object> dependencyBeans, ComponentManager componentManager) {
        T instance;
        try {
            constructor.setAccessible(true);
            instance = constructor.newInstance(dependencyBeans.toArray());
        } catch (InvocationTargetException e) {
            throw new ComponentInstantiationException(String.format(
                    "failed to construct component %s, error: %s",
                    clasz.getName(),
                    e.getTargetException().getMessage()),
                    e.getTargetException());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new ComponentInstantiationException(String.format(
                    "failed to construct component %s, error: %s",
                    clasz.getName(),
                    e.getMessage()),
                    e);
        }

        if (postConstruct != null) {
            postConstruct.setAccessible(true); // maybe a little naughty here
            try {
                postConstruct.invoke(instance);
            } catch (InvocationTargetException e) {
                throw new ComponentInstantiationException(String.format(
                        "failed to construct component %s, error: %s",
                        clasz.getName(),
                        e.getTargetException().getMessage()),
                        e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new ComponentInstantiationException(String.format(
                        "error invoking %s annotated method %s on component %s: %s",
                        PostConstruct.class.getName(),
                        postConstruct.getName(),
                        clasz.getName(),
                        e.getMessage())
                );
            }
        }

        for (ComponentConstructionInterceptor interceptor : interceptors) {
            instance = interceptor.intercept(instance, componentManager);
        }

        return instance;
    }
}
