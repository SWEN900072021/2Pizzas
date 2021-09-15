package com.twopizzas.configuration;

import com.twopizzas.di.ComponentConstructionInterceptor;
import com.twopizzas.di.ComponentManager;

import java.util.Arrays;

public class ConfigurationConstructionInterceptor implements ComponentConstructionInterceptor {

    private ConfigurationContext context;

    @Override
    public <T> T intercept(T component, ComponentManager componentManager) {
        if (component.getClass().getAnnotation(Configuration.class) != null) {
            fetchComponents(componentManager);
            Arrays.stream(component.getClass().getDeclaredFields()).forEach(
                    f -> {
                        Value valueAnnotation = f.getAnnotation(Value.class);
                        if (valueAnnotation != null) {
                            f.setAccessible(true);
                            String value = context.getConfigurationProperty(valueAnnotation.value());
                            try {

                                f.set(component, value);
                            } catch (Exception e) {
                                throw new ConfigurationContextException(String.format(
                                        "failed to initialize configuration property [%s] for configuration class [%s] with value " +
                                        "[%s] at [%s] in configuration file [%s], error: %s", f.getName(), component.getClass().getName(),
                                        value, valueAnnotation.value(), context.getFileName(), e.getMessage()));
                            }
                        }
                    }
            );
        }
        return component;
    }

    private void fetchComponents(ComponentManager componentManager) {
        if (context == null) {
            context = componentManager.getComponent(ConfigurationContext.class);
        }
    }
}
