package com.twopizzas.di;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Injector {
    private final ComponentLoader componentLoader;

    Injector(ComponentLoader componentLoader) {
        this.componentLoader = componentLoader;
    }

    Map<Class<?>, Object> injectAll() {
        Map<Class<?>, Object> injected = new HashMap<>();
        componentLoader.scanAllComponents().forEach(
                c -> inject(c, injected)
        );
        return injected;
    }

    private Object inject(Class<?> clasz, Map<Class<?>, Object> components) {
        // the component has already been constructed with all its dependencies so return it
        if (components.containsKey(clasz)) {
            return components.get(clasz);
        }

        // get all the dependencies for the component and construct
        List<Object> dependencies = componentLoader.getDependencies(clasz).stream().map(
                d -> inject(clasz, components)
        ).collect(Collectors.toList());

        return componentLoader.instantiate(clasz, dependencies);
    }
}
