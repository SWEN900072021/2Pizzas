package com.twopizzas.di;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class ApplicationContextComponent implements ApplicationContext {

    private final ComponentManager componentManager;
    private final String profile;

    ApplicationContextComponent(String profile, ComponentManager componentManager) {
        this.componentManager = componentManager;
        this.profile = profile;
    }

    @Override
    public <T> T getComponent(Class<T> componentClass) throws ApplicationContextException {
        return componentManager.getComponent(componentClass);
    }

    @Override
    public Collection<?> getComponentsAnnotatedWith(Class<? extends Annotation> annotationClass) {
        return componentManager.getComponents(new AnnotationSpecification(annotationClass));
    }

    @Override
    public <T> T getComponent(Class<T> componentClass, String qualifier) throws ApplicationContextException {
        return componentManager.getComponent(new QualifiedBeanSpecification<>(qualifier, new BaseBeanSpecification<>(componentClass)));
    }

    @Override
    public String getProfile() {
        return profile;
    }
}
