package com.twopizzas.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PropertiesUtilTests {

    @Mock
    private EnvironmentUtil environment;

    private PropertiesUtil propertiesUtil;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        propertiesUtil = new PropertiesUtil(environment);
    }

    @Test
    @DisplayName("GIVEN value with no environment variable WHEN resolveWithEnv invoked THEN value returned unchanged")
    void test1() {
        // GIVEN
        String value = "http://localhost:8080";

        // WHEN
        String resolved = propertiesUtil.resolveWithEnv(value);

        // THEN
        Assertions.assertEquals(value, resolved);
    }

    @Test
    @DisplayName("GIVEN value is exactly environment variable token and environment variable exists " +
            "WHEN resolveWithEnv invoked THEN environment variable value injected into return string")
    void test3() {
        // GIVEN
        String value = "${HOST_NAME}";
        Mockito.when(environment.getEnv(Mockito.any())).thenReturn(Optional.of("localhost"));

        // WHEN
        String resolved = propertiesUtil.resolveWithEnv(value);

        // THEN
        Assertions.assertEquals(value, resolved);
    }

    @Test
    @DisplayName("GIVEN value with one environment variable and environment variable exists " +
            "WHEN resolveWithEnv invoked THEN environment variable value injected into return string")
    void test2() {
        // GIVEN
        String value = "http://:8080";

        // WHEN
        String resolved = propertiesUtil.resolveWithEnv(value);

        // THEN
        Assertions.assertEquals(value, resolved);
    }
}
