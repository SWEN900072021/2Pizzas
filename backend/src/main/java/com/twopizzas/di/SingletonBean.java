package com.twopizzas.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

class SingletonBean<T> extends BaseBean<T> {

    private T instance;

    SingletonBean(Class<T> clasz, String qualifier, List<String> profiles, boolean primary, Constructor<T> constructor, List<ComponentSpecification<?>> dependencies, Method postConstruct) {
        super(clasz, qualifier, profiles, primary, constructor, dependencies, postConstruct);
    }

    @Override
    public T construct(ComponentManager componentManager) {
        if (instance == null) {
            instance = super.construct(componentManager);
        }
        return instance;
    }
}
