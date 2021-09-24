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

    private static final String ROOT = "com.twopizzas.testroot";

    @Mock
    private ComponentManager componentManager;

    private ApplicationContextImpl applicationContext;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        applicationContext = new ApplicationContextImpl()
                .root(ROOT)
                .componentManager(componentManager);
    }

    @Test
    @DisplayName("GIVEN no root set WHEN init invoked THEN throws")
    void test() {
        // GIVEN
        applicationContext = new ApplicationContextImpl()
                .componentManager(componentManager);

        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> applicationContext.init());
    }

    @Test
    @DisplayName("GIVEN root set WHEN init invoked THEN then create component manager")
    void test2() {
        // GIVEN
        applicationContext = new ApplicationContextImpl()
                .root(ROOT);

        // WHEN
        applicationContext.init();

        // THEN
        Assertions.assertNotNull(applicationContext.getComponentManager());
    }

    @Test
    @DisplayName("GIVEN root and componentManager set WHEN init invoked THEN componentManager initialized")
    void test3() {
        // WHEN
        applicationContext.init();

        // THEN
        Mockito.verify(componentManager).init();
    }

    @Test
    @DisplayName("WHEN getComponent with qualifier THEN returns component from componentManager")
    void test4() {
        // GIVEN
        Object component = new Object();
        Mockito.when(componentManager.getComponent(Mockito.isA(TypedComponentSpecification.class))).thenReturn(component);

        // WHEN
        Class<?> clasz = component.getClass();
        String qualifier = "qualifier";
        Object retrieved = applicationContext.getComponent(clasz, qualifier);

        // THEN
        ArgumentCaptor<TypedComponentSpecification<?>> captor = ArgumentCaptor.forClass(TypedComponentSpecification.class);
        Mockito.verify(componentManager).getComponent(captor.capture());
        Assertions.assertEquals(component, retrieved);

        Assertions.assertTrue(captor.getValue() instanceof QualifiedBeanSpecification);
        Assertions.assertEquals(component.getClass(), ((QualifiedBeanSpecification) captor.getValue()).getClasz());
        Assertions.assertEquals(qualifier, ((QualifiedBeanSpecification) captor.getValue()).getQualifier());
    }

    @Test
    @DisplayName("WHEN getComponent THEN returns component from componentManager")
    void test5() {
        // GIVEN
        Object component = new Object();
        Mockito.when(componentManager.getComponent(Mockito.isA(TypedComponentSpecification.class))).thenReturn(component);

        // WHEN
        Class<?> claz = component.getClass();
        Object retrieved = applicationContext.getComponent(claz);

        // THEN
        ArgumentCaptor<TypedComponentSpecification<?>> captor = ArgumentCaptor.forClass(TypedComponentSpecification.class);
        Mockito.verify(componentManager).getComponent(captor.capture());
        Assertions.assertEquals(component, retrieved);

        Assertions.assertTrue(captor.getValue() instanceof BaseBeanSpecification);
        Assertions.assertEquals(component.getClass(), ((BaseBeanSpecification) captor.getValue()).getClasz());
    }

    @Test
    @DisplayName("GIVEN profile set WHEN init invoked THEN then profile resolver built")
    void test6() {
        // GIVEN
        applicationContext = new ApplicationContextImpl()
                .profile("test")
                .root("someRoot");


        // WHEN
        applicationContext.init();

        // THEN
        Assertions.assertNotNull(applicationContext.getComponentManager());
        Assertions.assertTrue(applicationContext.getComponentManager().getBeanResolver() instanceof PrimaryBeanResolver);
        Assertions.assertTrue(((PrimaryBeanResolver) applicationContext.getComponentManager().getBeanResolver()).getWrapped() instanceof ProfileBeanResolver);
        Assertions.assertEquals("test", (((ProfileBeanResolver) ((PrimaryBeanResolver) applicationContext.getComponentManager().getBeanResolver()).getWrapped()).getProfile()));
    }
}
