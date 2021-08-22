package com.twopizzas.di;

public class ApplicationContext {

    private static ApplicationContext instance;
    private String root;
    private boolean initialized;

    private ComponentManager componentManager;

    private ApplicationContext() {}

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public ApplicationContext root(String root) {
        this.root = root;
        return this;
    }

    public ApplicationContext init() {
        if (initialized) {
            return this;
        }

        if (root == null) {
            throw new ApplicationContextException("init called on ApplicationContext with no root, set root prior to initializing the Application Context");
        }

        componentManager = new ComponentManager(root, new Injector(new ComponentLoader(root)));
        componentManager.init();
        initialized = true;
        return this;
    }

    public <T> T getComponent(Class<T> componentClass) throws ApplicationContextException {
        init();
        return componentManager.getComponent(componentClass);
    }
}
