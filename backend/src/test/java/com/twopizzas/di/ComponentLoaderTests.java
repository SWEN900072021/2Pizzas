package com.twopizzas.di;

import com.twopizzas.di.othertestroot.MultipleAutowires;
import com.twopizzas.di.othertestroot.MultiplePostConstruct;
import com.twopizzas.di.testroot.*;
import com.twopizzas.di.testroot.module.ModuleComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

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
    @DisplayName("GIVEN component has qualifier WHEN getBean invoked THEN returns bean with qualifier")
    void test1() {
        Bean<TestDependency> bean = componentLoader.loadBean(TestDependency.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertEquals("qualifier", bean.getQualifier());
    }

    @Test
    @DisplayName("GIVEN components has no qualifier WHEN getBean invoked THEN returns bean without qualifier")
    void test2() {
        Bean<TestDependencyOther> bean = componentLoader.loadBean(TestDependencyOther.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertNull(bean.getQualifier());
    }

    @Test
    @DisplayName("GIVEN component has no dependencies WHEN getDependencies invoked THEN returns empty")
    void test3() {
        // WHEN
        Bean<TestDependency> bean = componentLoader.loadBean(TestDependency.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertTrue(bean.getDependencies().isEmpty());
    }

    @Test
    @DisplayName("GIVEN component has no constructor annotated with @Autowired WHEN getDependencies invoked THEN returns empty")
    void test4() throws NoSuchMethodException {
        // WHEN
        Bean<AbstractComponentImpl> bean = componentLoader.loadBean(AbstractComponentImpl.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertTrue(bean.getDependencies().isEmpty());
        Assertions.assertEquals(AbstractComponentImpl.class.getDeclaredConstructor(), bean.getConstructor());
    }

    @Test
    @DisplayName("GIVEN component has constructor with parameters @Autowired WHEN getDependencies invoked THEN returns list of classes")
    void test5() throws NoSuchMethodException {
        // WHEN
        Bean<TestClientComponent> bean = componentLoader.loadBean(TestClientComponent.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertEquals(3, bean.getDependencies().size());
        Assertions.assertEquals(TestDependency.class, bean.getDependencies().get(0).getClasz());
        Assertions.assertEquals(TestDependencyOther.class, bean.getDependencies().get(1).getClasz());
        Assertions.assertEquals(InterfaceComponent.class, bean.getDependencies().get(2).getClasz());
        Assertions.assertEquals(TestClientComponent.class.getDeclaredConstructor(
                TestDependency.class, TestDependencyOther.class, InterfaceComponent.class),
                bean.getConstructor());
    }

    @Test
    @DisplayName("GIVEN component has constructor with parameters @Autowired and parameter has @Qualifier WHEN getDependencies invoked THEN returns list of classes")
    void test6() {
        // WHEN
        Bean<TestClientComponent> bean = componentLoader.loadBean(TestClientComponent.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertEquals(3, bean.getDependencies().size());
        Assertions.assertEquals(TestDependency.class, bean.getDependencies().get(0).getClasz());
        Assertions.assertTrue(bean.getDependencies().get(0) instanceof QualifiedBeanSpecification);
        Assertions.assertEquals("qualifier", ((QualifiedBeanSpecification) bean.getDependencies().get(0)).getQualifier());
    }

    @Test
    @DisplayName("GIVEN component has no default constructor and no constructor annotated with @Autowired WHEN loadBean invoked THEN throws")
    void test7() {
        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> componentLoader.loadBean(ComponentWithoutConstructor.class));
    }

    @Test
    @DisplayName("GIVEN component annotated with @Profile WHEN loadBean THEN profiles set")
    void test8() {
        // WHEN
        Bean<TestDependencyOther> bean = componentLoader.loadBean(TestDependencyOther.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(bean.getProfiles());
        Assertions.assertFalse(bean.getProfiles().isEmpty());
        Assertions.assertTrue(bean.getProfiles().contains("test"));
    }

    @Test
    @DisplayName("GIVEN component not annotated with @Profile WHEN loadBean THEN profiles empty")
    void test9() {
        // WHEN
        Bean<TestDependency> bean = componentLoader.loadBean(TestDependency.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(bean.getProfiles());
        Assertions.assertTrue(bean.getProfiles().isEmpty());
    }

    @Test
    @DisplayName("GIVEN component annotated with @Primary WHEN loadBean THEN isPrimary set to true")
    void test10() {
        // WHEN
        Bean<TestDependencyOther> bean = componentLoader.loadBean(TestDependencyOther.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertTrue(bean.isPrimary());
    }

    @Test
    @DisplayName("GIVEN component not annotated with @Primary WHEN loadBean THEN isPrimary set to true")
    void test11() {
        // WHEN
        Bean<TestDependency> bean = componentLoader.loadBean(TestDependency.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertFalse(bean.isPrimary());
    }

    @Test
    @DisplayName("GIVEN component has multiple constructors annotated with @Autowired WHEN loadBean invoked THEN throws")
    void test12() {
        // WHEN + THEN
        Assertions.assertThrows(ApplicationContextException.class, () -> componentLoader.loadBean(MultipleAutowires.class));
    }

    @Test
    @DisplayName("GIVEN component has method annotated with @PostConstruct WHEN loadBean invoked THEN returns bean with postConstruct")
    void test13() {
        // WHEN
        Bean<TestDependency> bean = componentLoader.loadBean(TestDependency.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(bean.getConstructor());
        Assertions.assertEquals(0, bean.getPostConstruct().getParameterTypes().length);
    }

    @Test
    @DisplayName("GIVEN component has no method annotated with @PostConstruct WHEN loadBean invoked THEN returns bean without postConstruct")
    void test14() {
        // WHEN
        Bean<TestDependencyOther> bean = componentLoader.loadBean(TestDependencyOther.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertNull(bean.getPostConstruct());
    }

    @Test
    @DisplayName("GIVEN component has multiple methods annotated with @PostConstruct WHEN loadBean invoked THEN returns no args")
    void test15() {
        // WHEN
        Bean<MultiplePostConstruct> bean = componentLoader.loadBean(MultiplePostConstruct.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(bean.getPostConstruct());
        Assertions.assertEquals(0, bean.getPostConstruct().getParameterTypes().length);
    }

    @Test
    @DisplayName("GIVEN component scope is PROTOTYPE WHEN loadBean THEN returns BaseBean instance")
    void test16() {
        // WHEN
        Bean<TestDependency> bean = componentLoader.loadBean(TestDependency.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertTrue(bean instanceof BaseBean);
    }

    @Test
    @DisplayName("GIVEN component scope is SINGLETON WHEN loadBean THEN returns SingletonBeanProxy wrapping BaseBean instance")
    void test17() {
        // WHEN
        Bean<TestDependencyOther> bean = componentLoader.loadBean(TestDependencyOther.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertTrue(bean instanceof SingletonBeanProxy);
        Assertions.assertNotNull(((SingletonBeanProxy<TestDependencyOther>) bean).getWrapped());
        Assertions.assertTrue(((SingletonBeanProxy<TestDependencyOther>) bean).getWrapped() instanceof BaseBean);
    }

    @Test
    @DisplayName("GIVEN component scope is PROTOTYPE and is threadlocal WHEN loadBean THEN returns ThreadLocalBeanProxy wrapping BaseBean instance")
    void test18() {
        // WHEN
        Bean<ThreadLocalComponentPrototype> bean = componentLoader.loadBean(ThreadLocalComponentPrototype.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertTrue(bean instanceof ThreadLocalBeanProxy);
        Assertions.assertNotNull(((ThreadLocalBeanProxy<ThreadLocalComponentPrototype>) bean).getWrapped());
        Assertions.assertTrue(((ThreadLocalBeanProxy<ThreadLocalComponentPrototype>) bean).getWrapped() instanceof BaseBean);
    }

    @Test
    @DisplayName("GIVEN component scope is SINGLETON and is threadlocal WHEN loadBean THEN returns SingletonBeanProxy wrapping ThreadLocalBeanProxy wrapping BaseBean instance")
    void test19() {
        // WHEN
        Bean<ThreadLocalComponentSingleton> bean = componentLoader.loadBean(ThreadLocalComponentSingleton.class);

        // THEN
        Assertions.assertNotNull(bean);
        Assertions.assertTrue(bean instanceof SingletonBeanProxy);
        Assertions.assertNotNull(((SingletonBeanProxy<ThreadLocalComponentSingleton>) bean).getWrapped());
        Assertions.assertTrue(((SingletonBeanProxy<ThreadLocalComponentSingleton>) bean).getWrapped() instanceof ThreadLocalBeanProxy);
        ThreadLocalBeanProxy<ThreadLocalComponentSingleton> wrapped = (ThreadLocalBeanProxy<ThreadLocalComponentSingleton>) ((SingletonBeanProxy<ThreadLocalComponentSingleton>) bean).getWrapped();
        Assertions.assertNotNull(wrapped.getWrapped());
        Assertions.assertTrue(wrapped.getWrapped() instanceof BaseBean);
    }

}
