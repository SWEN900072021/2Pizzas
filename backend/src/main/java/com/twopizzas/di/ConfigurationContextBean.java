package com.twopizzas.di;

import com.twopizzas.configuration.ConfigurationContext;
import com.twopizzas.configuration.ConfigurationContextImpl;
import com.twopizzas.util.EnvironmentUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

class ConfigurationContextBean implements Bean<ConfigurationContext> {

    private ConfigurationContext component;
    private final String profile;

    ConfigurationContextBean(String profile) {
        this.profile = profile;
    }

    @Override
    public Class<ConfigurationContext> getClasz() {
        return ConfigurationContext.class;
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
    public Constructor<ConfigurationContext> getConstructor() {
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
    public ConfigurationContext construct(ComponentManager componentManager) {
        if (component == null) {
            component = new ConfigurationContextImpl(new EnvironmentUtil(), profile);
            component.init();
        }
        return component;
    }
}
