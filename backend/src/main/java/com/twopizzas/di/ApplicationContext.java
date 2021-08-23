package com.twopizzas.di;

public class ApplicationContext {

    private static ApplicationContext instance;
    private String root;

    private ComponentManager componentManager;

    private ApplicationContext() {}

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public ApplicationContext root(String root) {
        this.root = root;
        return this;
    }

    // for testing only
    ApplicationContext componentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
        return instance;
    }

    public ApplicationContext init() {
        if (root == null) {
            throw new ApplicationContextException("init called on ApplicationContext with no root, set root prior to initializing the Application Context");
        }

        if (componentManager == null) {
            componentManager = new ComponentManager(new ComponentInjector(new ComponentLoader(root)), new ComponentStore());
        }
        componentManager.init();
        return this;
    }

    public <T> T getComponent(Class<T> componentClass) throws ApplicationContextException {
        return componentManager.getComponent(componentClass);
    }
}
