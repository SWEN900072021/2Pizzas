package com.twopizzas.di;

import com.twopizzas.util.AssertionConcern;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class ApplicationContextImpl extends AssertionConcern implements ApplicationContext {

    private static ApplicationContextImpl instance;
    private String root;
    private String profile;

    private ComponentManager componentManager;

    private ApplicationContextImpl() {}

    public static ApplicationContextImpl getInstance() {
        if (instance == null) {
            instance = new ApplicationContextImpl();
        }
        return instance;
    }

    public ApplicationContextImpl root(String root) {
        notBlank(notNull(root, "root"), "root");
        this.root = root;
        return this;
    }

    public ApplicationContextImpl profile(String profile) {
        notBlank(notNull(profile, "profile"), "profile");
        this.profile = profile;
        return this;
    }

    // for testing only
    ApplicationContextImpl componentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
        return instance;
    }

    // for testing only
    ComponentManager getComponentManager() {
        return componentManager;
    }

    public ApplicationContextImpl init() {
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

    @Override
    public <T> T getComponent(Class<T> componentClass) throws ApplicationContextException {
        notNull(componentClass, "componentClass");
        return componentManager.getComponent(new BaseBeanSpecification<>(componentClass));
    }

    @Override
    public Collection<?> getComponentsAnnotatedWith(Class<? extends Annotation> annotationClass) {
        notNull(annotationClass, "annotationClass");
        return componentManager.getComponents(new AnnotationSpecification(annotationClass));
    }

    @Override
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
