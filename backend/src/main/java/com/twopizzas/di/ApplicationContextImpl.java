package com.twopizzas.di;

import com.twopizzas.util.AssertionConcern;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class ApplicationContextImpl extends AssertionConcern implements ApplicationContext {

    private String root;
    private String profile;

    private ComponentManager componentManager;

    public ApplicationContextImpl() {}

    public ApplicationContextImpl root(String root) {
        notNullAndNotBlank(root, "root");
        this.root = root;
        return this;
    }

    public ApplicationContextImpl profile(String profile) {
        notNullAndNotBlank(profile, "profile");
        this.profile = profile;
        return this;
    }

    // for testing only
    ApplicationContextImpl componentManager(ComponentManager componentManager) {
        this.componentManager = componentManager;
        return this;
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
            componentManager.setApplicationContext(this);
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
        notNullAndNotBlank(qualifier, "qualifier");
        return componentManager.getComponent(
                new QualifiedBeanSpecification<>(qualifier, new BaseBeanSpecification<>(componentClass))
        );
    }

    @Override
    public String getProfile() {
        return profile;
    }

    private BeanResolver getBeanResolver() {
        BeanResolver beanResolver = new BaseBeanResolver();
        if (profile != null) {
            beanResolver = new ProfileBeanResolver(beanResolver, profile);
        }
        return new PrimaryBeanResolver(beanResolver);
    }
}
