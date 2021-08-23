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
public class ComponentManagerTests {

    private ComponentManager componentManager;

    @Mock
    private ComponentInjector componentInjector;

    @Mock
    private ComponentStore componentStore;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        componentManager = new ComponentManager(componentInjector, componentStore);
    }

    @Test
    @DisplayName("WHEN init invoked THEN componentInjector inject all components")
    void test() {
        // WHEN
        componentManager.init();

        // THEN
        Mockito.verify(componentInjector).injectAll(Mockito.eq(componentStore));
    }

    @Test
    @DisplayName("GIVEN component exists WHEN getComponent invoked THEN returns component")
    void test2() {
        // GIVEN
        StubComponent component = new StubComponent();
        Mockito.when(componentStore.get(Mockito.eq(StubComponent.class))).thenReturn(component);

        // WHEN
        StubComponent retrieved = componentManager.getComponent(StubComponent.class);

        // THEN
        Assertions.assertEquals(component, retrieved);
        Mockito.verify(componentStore).get(Mockito.eq(StubComponent.class));
    }

    @Test
    @DisplayName("GIVEN component does not exist WHEN getComponent invoked THEN throws")
    void test4() {
        // GIVEN
        Mockito.when(componentStore.get(Mockito.any())).thenReturn(null);

        // WHEN + THEN
        Assertions.assertThrows(ComponentNotFound.class, () -> componentManager.getComponent(StubComponent.class));
        Mockito.verify(componentStore).get(Mockito.eq(StubComponent.class));
    }

    private static class StubComponent {}

 }
