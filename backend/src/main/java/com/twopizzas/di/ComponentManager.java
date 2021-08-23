package com.twopizzas.di;

public class ComponentManager {
    private final ComponentInjector componentInjector;
    private ComponentStore componentStore;

    private boolean initialized;

    ComponentManager(ComponentInjector componentInjector, ComponentStore componentStore) {
        this.componentInjector = componentInjector;
        this.componentStore = componentStore;
    }

    void init() {
        if (!initialized) {
            componentStore = componentInjector.injectAll(componentStore);
        }
    }

    <T> T getComponent(Class<T> clasz) throws ApplicationContextException {
        T component = componentStore.get(clasz);
        if (component != null) {
            return unsafeCast(component);
        }

        throw new ComponentNotFound(clasz);
    }

    @SuppressWarnings({"unchecked"})
    private <T> T unsafeCast(Object o) {
        return (T) o;
    }
}
