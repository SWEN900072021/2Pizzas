package com.twopizzas.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

class ApplicationContextBean implements Bean<ApplicationContext> {

    private final ApplicationContext component;

    ApplicationContextBean(ApplicationContext context) {
        component = context;
    }

    @Override
    public Class<ApplicationContext> getClasz() {
        return ApplicationContext.class;
    }

    @Override
    public String getQualifier() {
        return null;
    }

    @Override
    public List<String> getProfiles() {
        return Collections.emptyList();
    }

    @Override
    public Constructor<ApplicationContext> getConstructor() {
        return null;
    }

    @Override
    public Method getPostConstruct() {
        return null;
    }

    @Override
    public boolean isPrimary() {
        return true;
    }

    @Override
    public List<TypedComponentSpecification<?>> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public ApplicationContext construct(ComponentManager componentManager) {
        return component;
    }
}
