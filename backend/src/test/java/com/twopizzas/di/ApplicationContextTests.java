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

    private ApplicationContextImpl applicationContext;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        applicationContext = ApplicationContextImpl.getInstance()
                .root(ROOT)
                .componentManager(componentManager);
    }

    @Test
    @DisplayName("GIVEN no root set WHEN init invoked THEN throws")
    void test() {
        // GIVEN
        ApplicationContextImpl.reset();

        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> ApplicationContextImpl.getInstance().init());
    }

    @Test
    @DisplayName("GIVEN root set WHEN init invoked THEN then create component manager")
    void test2() {
        // GIVEN
        ApplicationContextImpl.reset();
        ApplicationContextImpl.getInstance().root("someRoot");

        // WHEN
        ApplicationContextImpl.getInstance().init();

        // THEN
        Assertions.assertNotNull(ApplicationContextImpl.getInstance().getComponentManager());
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
        Class<?> claz = component.getClass();
        String qualifier = "qualifier";
        Object retrieved = applicationContext.getComponent(claz, qualifier);

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
        ApplicationContextImpl.reset();
        ApplicationContextImpl.getInstance()
                .profile("profile")
                .root("someRoot");


        // WHEN
        ApplicationContextImpl.getInstance().init();

        // THEN
        Assertions.assertNotNull(ApplicationContextImpl.getInstance().getComponentManager());
        Assertions.assertTrue(ApplicationContextImpl.getInstance().getComponentManager().getBeanResolver() instanceof PrimaryBeanResolver);
        Assertions.assertTrue(((PrimaryBeanResolver) ApplicationContextImpl.getInstance().getComponentManager().getBeanResolver()).getWrapped() instanceof ProfileBeanResolver);
        Assertions.assertEquals("profile", (((ProfileBeanResolver) ((PrimaryBeanResolver) ApplicationContextImpl.getInstance().getComponentManager().getBeanResolver()).getWrapped()).getProfile()));
    }
}
