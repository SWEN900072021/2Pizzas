package com.twopizzas.configuration;

public interface ConfigurationContext {
    void init();
    String getConfigurationProperty(String name);
    String getFileName();
}
