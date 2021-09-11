package com.twopizzas.di;

import com.twopizzas.util.AssertionConcern;

public class ApplicationContext extends AssertionConcern {

    private static ApplicationContext instance;
    private String root;
    private String profile;

    private ComponentManager componentManager;

    private ApplicationContext() {}

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public ApplicationContext root(String root) {
        notBlank(notNull(root, "root"), "root");
        this.root = root;
        return this;
    }

    public ApplicationContext profile(String profile) {
        notBlank(notNull(profile, "profile"), "profile");
        this.profile = profile;
        return this;
    }

    // for testing only
    ApplicationContext componentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
        return instance;
    }

    // for testing only
    ComponentManager getComponentManager() {
        return componentManager;
    }

    public ApplicationContext init() {
        if (root == null) {
            throw new ApplicationContextException("init called on ApplicationContext with no root, set root prior to initializing the Application Context");
        }

        if (componentManager == null) {
            componentManager = new ComponentManager(
                    getBeanResolver(),
                    new ComponentLoader(root)
            );
        }
        componentManager.init();
        return this;
    }

    public <T> T getComponent(Class<T> componentClass) throws ApplicationContextException {
        notNull(componentClass, "componentClass");
        return componentManager.getComponent(new BaseBeanSpecification<>(componentClass));
    }

    public <T> T getComponent(Class<T> componentClass, String qualifier) throws ApplicationContextException {
        notNull(componentClass, "componentClass");
        notBlank(notNull(qualifier, "qualifier"), "qualifier");
        return componentManager.getComponent(
                new QualifiedBeanSpecification<>(qualifier, new BaseBeanSpecification<>(componentClass))
        );
    }

    private BeanResolver getBeanResolver() {
        BeanResolver beanResolver = new BaseBeanResolver();
        if (profile != null) {
            beanResolver = new ProfileBeanResolver(beanResolver, profile);
        }
        return new PrimaryBeanResolver(beanResolver);
    }

    // for testing only
    static void reset() {
        instance = null;
    }
}
