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

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class ComponentManagerTests {

    private ComponentManager componentManager;

    @Mock
    private BeanLoader beanLoader;

    @Mock
    private BeanResolver beanResolver;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        componentManager = new ComponentManager(beanResolver, beanLoader);
        componentManager.setApplicationContext(new ApplicationContextImpl().root("someRoot").componentManager(componentManager));
    }

    @Test
    @DisplayName("WHEN init invoked THEN componentInjector inject all components")
    void test() {
        // WHEN
        componentManager.init();

        // THEN
        Mockito.verify(beanLoader).load();
    }

    @Test
    @DisplayName("GIVEN component exists WHEN getComponent invoked THEN returns component")
    void test2() {
        // GIVEN
        StubComponent component = new StubComponent();
        Bean<StubComponent> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.construct(Mockito.any())).thenReturn(component);
        Mockito.when(beanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(
                arg -> Collections.singletonList(bean)
        );

        // WHEN
        TypedComponentSpecification<StubComponent> specification = Mockito.mock(TypedComponentSpecification.class);
        StubComponent retrieved = componentManager.getComponent(specification);

        // THEN
        Assertions.assertEquals(component, retrieved);
        Mockito.verify(beanResolver).resolve(Mockito.eq(specification), Mockito.any());
        Mockito.verify(bean).construct(Mockito.eq(componentManager));
    }

    @Test
    @DisplayName("GIVEN component does not exist WHEN getComponent invoked THEN throws")
    void test3() {
        // GIVEN
        Mockito.when(beanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(
                arg -> Collections.emptyList()
        );

        // WHEN + THEN
        TypedComponentSpecification<StubComponent> specification = Mockito.mock(TypedComponentSpecification.class);
        Mockito.when(specification.getClasz()).thenReturn(StubComponent.class);
        Assertions.assertThrows(ComponentNotFound.class, () -> componentManager.getComponent(specification));

        // THEN
        Mockito.verify(beanResolver).resolve(Mockito.eq(specification), Mockito.any());
    }

    @Test
    @DisplayName("GIVEN multiple components satisfy specification WHEN getComponent invoked THEN throws")
    void test4() {
        // GIVEN
        Bean<StubComponent> bean = Mockito.mock(Bean.class);
        Mockito.when(bean.getClasz()).thenReturn(StubComponent.class);
        Bean<StubComponent> bean2 = Mockito.mock(Bean.class);
        Mockito.when(bean2.getClasz()).thenReturn(StubComponent.class);
        Mockito.when(beanResolver.resolve(Mockito.any(), Mockito.any())).thenAnswer(
                arg -> Arrays.asList(bean, bean2)
        );

        // WHEN + THEN
        TypedComponentSpecification<StubComponent> specification = Mockito.mock(TypedComponentSpecification.class);
        Assertions.assertThrows(DuplicateComponentException.class, () -> componentManager.getComponent(specification));

        // THEN
        Mockito.verify(beanResolver).resolve(Mockito.eq(specification), Mockito.any());
    }

    private static class StubComponent {}

 }
