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
    @DisplayName("GIVEN no root set WHEN init invoked THEN throws")
    void test() {
        // GIVEN
        ApplicationContext.reset();

        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> ApplicationContext.getInstance().init());
    }

    @Test
    @DisplayName("GIVEN root set WHEN init invoked THEN then create component manager")
    void test2() {
        // GIVEN
        ApplicationContext.reset();
        ApplicationContext.getInstance().root("someRoot");

        // WHEN
        ApplicationContext.getInstance().init();

        // THEN
        Assertions.assertNotNull(ApplicationContext.getInstance().getComponentManager());
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
        Mockito.when(componentManager.getComponent(Mockito.isA(ComponentSpecification.class))).thenReturn(component);

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

    @Test
    @DisplayName("WHEN getComponent THEN returns component from componentManager")
    void test5() {
        // GIVEN
        Object component = new Object();
        Mockito.when(componentManager.getComponent(Mockito.isA(ComponentSpecification.class))).thenReturn(component);

        // WHEN
        Class<?> claz = component.getClass();
        Object retrieved = applicationContext.getComponent(claz);

        // THEN
        ArgumentCaptor<ComponentSpecification<?>> captor = ArgumentCaptor.forClass(ComponentSpecification.class);
        Mockito.verify(componentManager).getComponent(captor.capture());
        Assertions.assertEquals(component, retrieved);

        Assertions.assertTrue(captor.getValue() instanceof BaseBeanSpecification);
        Assertions.assertEquals(component.getClass(), ((BaseBeanSpecification) captor.getValue()).getClasz());
    }

    @Test
    @DisplayName("GIVEN profile set WHEN init invoked THEN then profile resolver built")
    void test6() {
        // GIVEN
        ApplicationContext.reset();
        ApplicationContext.getInstance()
                .profile("profile")
                .root("someRoot");


        // WHEN
        ApplicationContext.getInstance().init();

        // THEN
        Assertions.assertNotNull(ApplicationContext.getInstance().getComponentManager());
        Assertions.assertTrue(ApplicationContext.getInstance().getComponentManager().getBeanResolver() instanceof PrimaryBeanResolver);
        Assertions.assertTrue(((PrimaryBeanResolver) ApplicationContext.getInstance().getComponentManager().getBeanResolver()).getWrapped() instanceof ProfileBeanResolver);
        Assertions.assertEquals("profile", (((ProfileBeanResolver) ((PrimaryBeanResolver) ApplicationContext.getInstance().getComponentManager().getBeanResolver()).getWrapped()).getProfile()));
    }
}
