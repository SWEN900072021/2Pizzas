package com.twopizzas.configuration;

import com.twopizzas.di.ComponentManager;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ConfigurationConstructionInterceptorTests {

    @Mock
    private ComponentManager manager;

    @Mock
    private ConfigurationContext context;

    private ConfigurationConstructionInterceptor interceptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(manager.getComponent(Mockito.argThat(a -> a.equals(ConfigurationContext.class)))).thenReturn(context);
        interceptor = new ConfigurationConstructionInterceptor();
    }

    @Test
    @DisplayName("GIVEN configuration context initialized WHEN intercept configuration class THEN injects configuration properties")
    void test() {
        // GIVEN
        Mockito.when(context.getConfigurationProperty("test.property")).thenReturn("someValue");
        TestConfiguration testConfiguration = new TestConfiguration();

        // WHEN
        TestConfiguration intercepted = interceptor.intercept(testConfiguration, manager);

        // THEN
        Assertions.assertNotNull(intercepted);
        Assertions.assertEquals("someValue", testConfiguration.getTestProperty());
        Assertions.assertEquals("notChanged", testConfiguration.getNotAProperty());
        Mockito.verify(context).getConfigurationProperty(Mockito.eq("test.property"));
        Mockito.verifyNoMoreInteractions(context);
    }

    @Test
    @DisplayName("GIVEN configuration context initialized WHEN intercept non configuration class THEN does not inject configuration properties")
    void test2() {
        // GIVEN
        Mockito.when(context.getConfigurationProperty("test.property")).thenReturn("someValue");
        TestNonConfiguration testConfiguration = new TestNonConfiguration();

        // WHEN
        TestNonConfiguration intercepted = interceptor.intercept(testConfiguration, manager);

        // THEN
        Assertions.assertNotNull(intercepted);
        Assertions.assertEquals("notChanged", testConfiguration.getTestProperty());
        Assertions.assertEquals("notChanged", testConfiguration.getNotAProperty());
        Mockito.verify(context, Mockito.never()).getConfigurationProperty(Mockito.any());
    }

    @Configuration
    @Getter
    static class TestConfiguration {
        @Value("test.property")
        private String testProperty;

        private final String notAProperty = "notChanged";
    }

    @Getter
    static class TestNonConfiguration {
        @Value("test.property")
        private final String testProperty = "notChanged";

        private final String notAProperty = "notChanged";
    }

}
