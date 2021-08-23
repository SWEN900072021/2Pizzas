package com.twopizzas.di;

import com.twopizzas.di.othertestroot.BadClientComponent;
import com.twopizzas.di.othertestroot.NotImplemented;
import com.twopizzas.di.testroot.*;
import com.twopizzas.di.testroot.module.ModuleComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ComponentLoaderTests {

    private static final String ROOT = "com.twopizzas.di.testroot";

    private ComponentLoader componentLoader;

    @BeforeEach
    void setup() {
        componentLoader = new ComponentLoader(ROOT);
    }

    @Test
    @DisplayName("GIVEN components in root WHEN scanAllComponents invoked THEN returns list of components")
    void test() {
        // WHEN
        Collection<Class<?>> componentClasses = componentLoader.scanAllComponents();

        // THEN
        Assertions.assertNotNull(componentClasses);
        Assertions.assertEquals(6, componentClasses.size());
        Assertions.assertTrue(componentClasses.contains(TestClientComponent.class));
        Assertions.assertTrue(componentClasses.contains(TestDependency.class));
        Assertions.assertTrue(componentClasses.contains(TestDependencyOther.class));
        Assertions.assertTrue(componentClasses.contains(ModuleComponent.class));
        Assertions.assertTrue(componentClasses.contains(InterfaceComponentImpl.class));
        Assertions.assertTrue(componentClasses.contains(AbstractComponentImpl.class));
    }

    @Test
    @DisplayName("GIVEN component has no dependencies WHEN getDependencies invoked THEN returns empty")
    void test2() {
        // WHEN
        List<Class<?>> dependencies = componentLoader.getDependencies(TestDependency.class);

        // THEN
        Assertions.assertTrue(dependencies.isEmpty());
    }

    @Test
    @DisplayName("GIVEN component has no constructor annotated with @Autowired WHEN getDependencies invoked THEN returns empty")
    void test3() {
        // WHEN
        List<Class<?>> dependencies = componentLoader.getDependencies(AbstractComponentImpl.class);

        // THEN
        Assertions.assertNotNull(dependencies);
        Assertions.assertTrue(dependencies.isEmpty());
    }

    @Test
    @DisplayName("GIVEN component has constructor with parameters @Autowired WHEN getDependencies invoked THEN returns list of classes")
    void test4() {
        // WHEN
        List<Class<?>> dependencies = componentLoader.getDependencies(TestClientComponent.class);

        // THEN
        Assertions.assertNotNull(dependencies);
        Assertions.assertEquals(3, dependencies.size());
        Assertions.assertEquals(TestDependency.class, dependencies.get(0));
        Assertions.assertEquals(TestDependencyOther.class, dependencies.get(1));
        Assertions.assertEquals(InterfaceComponentImpl.class, dependencies.get(2));
    }

    @Test
    @DisplayName("GIVEN component has no default constructor and no constructor annotated with @Autowired WHEN getDependencies invoked THEN throws")
    void test5() {
        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> componentLoader.getDependencies(ComponentWithoutConstructor.class));
    }

    @Test
    @DisplayName("GIVEN class is concrete component with no sub types WHEN getConcreteComponent invoked THEN returns that class")
    void test6() {
        // WHEN
        Class<? extends TestDependency> concreteClass = componentLoader.getConcreteComponent(TestDependency.class);

        // THEN
        Assertions.assertNotNull(concreteClass);
        Assertions.assertEquals(TestDependency.class, concreteClass);
    }

    @Test
    @DisplayName("GIVEN class is interface with single component implementation WHEN getConcreteComponent invoked THEN returns implementation")
    void test7() {
        // WHEN
        Class<? extends InterfaceComponent> concreteClass = componentLoader.getConcreteComponent(InterfaceComponent.class);

        // THEN
        Assertions.assertNotNull(concreteClass);
        Assertions.assertEquals(InterfaceComponentImpl.class, concreteClass);
    }

    @Test
    @DisplayName("GIVEN class is abstract with single component implementation WHEN getConcreteComponent invoked THEN returns implementation")
    void test8() {
        // WHEN
        Class<? extends AbstractComponent> concreteClass = componentLoader.getConcreteComponent(AbstractComponent.class);

        // THEN
        Assertions.assertNotNull(concreteClass);
        Assertions.assertEquals(AbstractComponentImpl.class, concreteClass);
    }

    @Test
    @DisplayName("GIVEN class has multiple component implementations WHEN getConcreteComponent invoked THEN throws")
    void test9() {
        // GIVEN
        componentLoader = new ComponentLoader("com.twopizzas.di.othertestroot");

        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> componentLoader.getConcreteComponent(BadClientComponent.class));
    }

    @Test
    @DisplayName("GIVEN class has no component implementations WHEN getConcreteComponent invoked THEN throws")
    void test10() {
        // GIVEN
        componentLoader = new ComponentLoader("com.twopizzas.di.othertestroot");

        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> componentLoader.getConcreteComponent(NotImplemented.class));
    }

    @Test
    @DisplayName("GIVEN class and correct dependencies WHEN construct invoked THEN returns instance")
    void test11() {
        // GIVEN
        TestDependency dependency = new TestDependency();
        TestDependencyOther dependencyOther = new TestDependencyOther();
        InterfaceComponent interfaceComponent = new InterfaceComponentImpl();

        // WHEN
        TestClientComponent component = componentLoader.construct(TestClientComponent.class, Arrays.asList(
                dependency, dependencyOther, interfaceComponent
        ));

        // THEN
        Assertions.assertNotNull(component);
        Assertions.assertEquals(dependency, component.getTestDependency());
        Assertions.assertEquals(dependencyOther, component.getTestDependencyOther());
        Assertions.assertEquals(interfaceComponent, component.getInterfaceComponent());
    }
}
