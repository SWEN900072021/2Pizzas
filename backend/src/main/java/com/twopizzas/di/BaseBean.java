package com.twopizzas.di;

import com.twopizzas.util.AssertionConcern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

class BaseBean<T> extends AssertionConcern implements Bean<T> {

    private final Constructor<T> constructor;
    private final List<ComponentSpecification<?>> dependencies;
    private final Method postConstruct;
    private final Class<T> clasz;
    private final String qualifier;
    private final List<String> profiles;
    private final boolean primary;

    BaseBean(Class<T> clasz,
             String qualifier,
             List<String> profiles,
             boolean primary,
             Constructor<T> constructor,
             List<ComponentSpecification<?>> dependencies,
             Method postConstruct)
    {
        this.clasz = clasz;
        this.qualifier = qualifier;
        this.profiles = profiles;
        this.constructor = constructor;
        this.dependencies = dependencies;
        this.postConstruct = postConstruct;
        this.primary = primary;
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
    public List<ComponentSpecification<?>> getDependencies() { return dependencies; }

    @Override
    public T construct(ComponentManager componentManager) {
        List<Object> dependencyInstances = dependencies.stream().map(componentManager::getBean)
                .map(b -> b.construct(componentManager)).collect(Collectors.toList());
        return build(dependencyInstances);
    }

    protected T build(List<Object> dependencyBeans) {
        T instance;
        try {
            constructor.setAccessible(true);
            instance = constructor.newInstance(dependencyBeans.toArray());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ComponentInstantiationException(String.format(
                    "failed to construct component %s, error: %s",
                    clasz.getName(),
                    e.getMessage()
            ));
        }

        if (postConstruct != null) {
            postConstruct.setAccessible(true); // maybe a little naughty here
            try {
                postConstruct.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ComponentInstantiationException(String.format(
                        "error invoking %s annotated method %s on component %s: %s",
                        PostConstruct.class.getName(),
                        postConstruct.getName(),
                        clasz.getName(),
                        e.getMessage())
                );
            }
        }
        return instance;
    }
}
