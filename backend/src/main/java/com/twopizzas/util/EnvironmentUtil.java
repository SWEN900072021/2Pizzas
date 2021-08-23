package com.twopizzas.util;

import com.twopizzas.di.Component;

import java.util.Optional;

@Component
public class EnvironmentUtil {
    public Optional<String> getEnv(String name) {
        String value = System.getenv(name);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }
}
