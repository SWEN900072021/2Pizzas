package com.twopizzas.configuration;

import com.twopizzas.util.EnvironmentUtil;
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
public class ConfigurationContextImplTests {

    @Mock
    private EnvironmentUtil environment;

    private ConfigurationContextImpl configurationManager;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        configurationManager = new ConfigurationContextImpl(environment, "profile");
    }

    @Test
    @DisplayName("GIVEN value with no environment variable WHEN resolveWithEnv invoked THEN value returned unchanged")
    void test1() {
        // GIVEN
        String value = "http://localhost:8080";

        // WHEN
        String resolved = configurationManager.resolveWithEnv(value);

        // THEN
        Assertions.assertEquals(value, resolved);
    }

    @Test
    @DisplayName("GIVEN value is exactly environment variable token and environment variable exists " +
            "WHEN resolveWithEnv invoked THEN environment variable value injected into return string")
    void test2() {
        // GIVEN
        String value = "${HOST_NAME}";
        Mockito.when(environment.getEnv(Mockito.any())).thenReturn(Optional.of("localhost"));

        // WHEN
        String resolved = configurationManager.resolveWithEnv(value);

        // THEN
        Assertions.assertEquals("localhost", resolved);
    }

    @Test
    @DisplayName("GIVEN value with one environment variable and environment variable exists " +
            "WHEN resolveWithEnv invoked THEN environment variable value injected into return string")
    void test3() {
        // GIVEN
        String value = "http://${HOST_NAME}:8080";
        Mockito.when(environment.getEnv(Mockito.any())).thenReturn(Optional.of("localhost"));

        // WHEN
        String resolved = configurationManager.resolveWithEnv(value);

        // THEN
        Assertions.assertEquals("http://localhost:8080", resolved);
    }

    @Test
    @DisplayName("GIVEN value with two environment variables and environment variables exist " +
            "WHEN resolveWithEnv invoked THEN environment variables value injected into return string")
    void test4() {
        // GIVEN
        String value = "http://${HOST_NAME}:${PORT}";
        Mockito.doReturn(Optional.of("localhost")).when(environment).getEnv(Mockito.eq("HOST_NAME"));
        Mockito.doReturn(Optional.of("8080")).when(environment).getEnv(Mockito.eq("PORT"));

        // WHEN
        String resolved = configurationManager.resolveWithEnv(value);

        // THEN
        Assertions.assertEquals("http://localhost:8080", resolved);
    }

    @Test
    @DisplayName("GIVEN profile is null WHEN init invoked THEN configuration read from application.properties")
    void test5() {
        // GIVEN
        configurationManager = new ConfigurationContextImpl(environment, null);

        // WHEN;
        configurationManager.init();

        // THEN
        Assertions.assertEquals("jdbc:postgresql://locahost:5432/postgres", configurationManager.getConfigurationProperty("datasource.url"));
        Assertions.assertEquals("postgres", configurationManager.getConfigurationProperty("datasource.username"));
        Assertions.assertEquals("password", configurationManager.getConfigurationProperty("datasource.password"));
    }

    @Test
    @DisplayName("GIVEN profile is test WHEN init invoked THEN configuration read from application-test.properties")
    void test6() {
        // GIVEN
        configurationManager = new ConfigurationContextImpl(environment, "test");
        Mockito.doReturn(Optional.of("testUsernameValue")).when(environment).getEnv(Mockito.eq("DATABASE_USERNAME"));
        Mockito.doReturn(Optional.of("testPasswordValue")).when(environment).getEnv(Mockito.eq("DATABASE_PASSWORD"));

        // WHEN;
        configurationManager.init();

        // THEN
        Assertions.assertNotNull(configurationManager.getConfigurationProperty("datasource.url"));
        Assertions.assertEquals("testUsernameValue", configurationManager.getConfigurationProperty("datasource.username"));
        Assertions.assertEquals("testPasswordValue", configurationManager.getConfigurationProperty("datasource.password"));
    }

}
