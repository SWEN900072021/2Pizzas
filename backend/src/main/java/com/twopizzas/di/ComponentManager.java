package com.twopizzas.di;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {
    private final Injector injector;
    private Map<Class<?>, Object> componentMap = new HashMap<>();

    ComponentManager(String root, Injector injector) {
        this.injector = injector;
    }

    void init() {
        componentMap = injector.injectAll();
    }

    <T> T getComponent(Class<T> clasz) throws ApplicationContextException {
        if (componentMap.containsKey(clasz)) {
            // we have built the map ourselves, this is safe
            return unsafeCast(componentMap.get(clasz));
        }
        throw new ComponentNotFound(clasz);
    }

    @SuppressWarnings({"unchecked"})
    private <T> T unsafeCast(Object o) {
        return (T) o;
    }
}
