package com.twopizzas.di;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentInjector {
    private final ComponentLoader componentLoader;

    ComponentInjector(ComponentLoader componentLoader) {
        this.componentLoader = componentLoader;
    }

    ComponentStore injectAll(ComponentStore componentStore) {
        componentLoader.scanAllComponents().forEach(
                c -> inject(c, componentStore)
        );
        return componentStore;
    }

    private Object inject(Class<?> clasz, ComponentStore components) {
        // the component has already been constructed with all its dependencies so return it
        Object component = components.get(clasz);
        if (component != null) {
            return component;
        }

        // get all the dependencies for the component and construct
        List<Object> dependencies = componentLoader.getDependencies(clasz).stream().map(
                d -> inject(d, components)
        ).collect(Collectors.toList());

        Object constructed = componentLoader.construct(clasz, dependencies);
        components.register(constructed);
        return constructed;
    }
}
