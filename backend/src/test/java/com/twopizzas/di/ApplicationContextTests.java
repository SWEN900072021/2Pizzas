package com.twopizzas.di;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ApplicationContextTests {

    private static final String ROOT = "com.twopizzas";

    @Mock
    private ComponentManager componentManager;

    private ApplicationContext applicationContext;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        applicationContext = ApplicationContext.getInstance()
                .root(ROOT)
                .componentManager(componentManager);
    }

    @Test
    @DisplayName("GIVEN root and componentManager set WHEN init invoked THEN componentManager initialized")
    void test() {
        // WHEN
        applicationContext.init();

        // THEN
        Mockito.verify(componentManager).init();
    }

    @Test
    @DisplayName("GIVEN context has component loaded WHEN getComponent THEN returns component from componentManager")
    void test1() {
        // GIVEN
        Object component = new Object();
        Mockito.when(componentManager.getComponent(Mockito.any())).thenReturn(component);

        // WHEN
        Class<?> claz = component.getClass();
        String qualifier = "qualifier";
        Object retrieved = applicationContext.getComponent(claz, qualifier);

        // THEN
        ArgumentCaptor<ComponentSpecification<?>> captor = ArgumentCaptor.forClass(ComponentSpecification.class);
        Mockito.verify(componentManager).getComponent(captor.capture());
        Assertions.assertEquals(component, retrieved);

        Assertions.assertTrue(captor.getValue() instanceof QualifiedBeanSpecification);
        Assertions.assertEquals(component.getClass(), ((QualifiedBeanSpecification) captor.getValue()).getClasz());
        Assertions.assertEquals(qualifier, ((QualifiedBeanSpecification) captor.getValue()).getQualifier());
    }
}
