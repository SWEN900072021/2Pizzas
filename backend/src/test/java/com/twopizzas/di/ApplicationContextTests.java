package com.twopizzas.di;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        Object retrieved = applicationContext.getComponent(claz);

        // THEN
        Mockito.verify(componentManager).getComponent(Mockito.eq(claz));
        Assertions.assertEquals(component, retrieved);
    }
}
