package com.twopizzas.di;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface ApplicationContext {
    <T> T getComponent(Class<T> componentClass) throws ApplicationContextException;
    Collection<?> getComponentsAnnotatedWith(Class<? extends Annotation> annotationClass);
    <T> T getComponent(Class<T> componentClass, String qualifier) throws ApplicationContextException;
    String getProfile();
}
