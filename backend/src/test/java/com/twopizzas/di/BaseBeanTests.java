package com.twopizzas.di;

import com.twopizzas.di.testroot.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;

public class BaseBeanTests {

    @Test
    @DisplayName("GIVEN bean has dependencies WHEN construct invoked THEN returns instance")
    void test() {
        // GIVEN
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);

        BaseBean<TestDependency> dependencyBean = Mockito.mock(BaseBean.class);
        TestDependency testDependency = new TestDependency();
        Mockito.when(dependencyBean.construct(Mockito.any())).thenReturn(testDependency);
        ComponentSpecification<TestDependency> testDependencyComponentSpecification = new BaseBeanSpecification<>(TestDependency.class);
        Mockito.doReturn(dependencyBean).when(componentManager)
                .getBean(Mockito.argThat(a -> a.getClasz().equals(TestDependency.class)));

        BaseBean<TestDependencyOther> dependencyOtherBean = Mockito.mock(BaseBean.class);
        TestDependencyOther testDependencyOther = new TestDependencyOther();
        Mockito.when(dependencyOtherBean.construct(Mockito.any())).thenReturn(testDependencyOther);
        ComponentSpecification<TestDependencyOther> testDependencyOtherComponentSpecification = new BaseBeanSpecification<>(TestDependencyOther.class);
        Mockito.doReturn(dependencyOtherBean).when(componentManager)
                .getBean(Mockito.argThat(a -> a.getClasz().equals(TestDependencyOther.class)));

        BaseBean<InterfaceComponent> interfaceComponentBean = Mockito.mock(BaseBean.class);
        InterfaceComponent interfaceComponent = new InterfaceComponentImpl();
        Mockito.when(interfaceComponentBean.construct(Mockito.any())).thenReturn(interfaceComponent);
        ComponentSpecification<InterfaceComponent> interfaceComponentComponentSpecification = new BaseBeanSpecification<>(InterfaceComponent.class);
        Mockito.doReturn(interfaceComponentBean).when(componentManager)
                .getBean(Mockito.argThat(a -> a.getClasz().equals(InterfaceComponent.class)));

        BaseBean<TestClientComponent> bean = new BaseBean<>(
                TestClientComponent.class,
                null,
                Collections.emptyList(),
                false,
                (Constructor<TestClientComponent>) TestClientComponent.class.getDeclaredConstructors()[0],
                Arrays.asList(testDependencyComponentSpecification, testDependencyOtherComponentSpecification, interfaceComponentComponentSpecification),
                null

        );

        // WHEN
        TestClientComponent instance = bean.construct(componentManager);

        // THEN
        Mockito.verify(dependencyBean).construct(Mockito.eq(componentManager));
        Mockito.verify(dependencyOtherBean).construct(Mockito.eq(componentManager));
        Mockito.verify(interfaceComponentBean).construct(Mockito.eq(componentManager));
        Assertions.assertNotNull(instance);
        Assertions.assertEquals(testDependency, instance.getTestDependency());
        Assertions.assertEquals(testDependencyOther, instance.getTestDependencyOther());
        Assertions.assertEquals(interfaceComponent, instance.getInterfaceComponent());
    }

    @Test
    @DisplayName("GIVEN bean has postConstruct WHEN construct invoked THEN postConstruct invoked")
    void test2() throws NoSuchMethodException {
        // GIVEN
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);

        BaseBean<TestDependency> bean = new BaseBean<>(
                TestDependency.class,
                null,
                Collections.emptyList(),
                false,
                (Constructor<TestDependency>) TestDependency.class.getDeclaredConstructors()[0],
                Collections.emptyList(),
                TestDependency.class.getDeclaredMethod("init")
        );

        // WHEN
        TestDependency instance = bean.construct(componentManager);

        // THEN
        Assertions.assertNotNull(instance);
        Assertions.assertTrue(instance.isInitialized());
    }

    @Test
    @DisplayName("GIVEN bean has dependencies WHEN construct invoked multiple times THEN returns new instance each time")
    void test3() {
        // GIVEN
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);

        BaseBean<TestDependency> dependencyBean = Mockito.mock(BaseBean.class);
        TestDependency testDependency = new TestDependency();
        Mockito.when(dependencyBean.construct(Mockito.any())).thenReturn(testDependency);
        ComponentSpecification<TestDependency> testDependencyComponentSpecification = new BaseBeanSpecification<>(TestDependency.class);
        Mockito.doReturn(dependencyBean).when(componentManager)
                .getBean(Mockito.argThat(a -> a.getClasz().equals(TestDependency.class)));

        BaseBean<TestDependencyOther> dependencyOtherBean = Mockito.mock(BaseBean.class);
        TestDependencyOther testDependencyOther = new TestDependencyOther();
        Mockito.when(dependencyOtherBean.construct(Mockito.any())).thenReturn(testDependencyOther);
        ComponentSpecification<TestDependencyOther> testDependencyOtherComponentSpecification = new BaseBeanSpecification<>(TestDependencyOther.class);
        Mockito.doReturn(dependencyOtherBean).when(componentManager)
                .getBean(Mockito.argThat(a -> a.getClasz().equals(TestDependencyOther.class)));

        BaseBean<InterfaceComponent> interfaceComponentBean = Mockito.mock(BaseBean.class);
        InterfaceComponent interfaceComponent = new InterfaceComponentImpl();
        Mockito.when(interfaceComponentBean.construct(Mockito.any())).thenReturn(interfaceComponent);
        ComponentSpecification<InterfaceComponent> interfaceComponentComponentSpecification = new BaseBeanSpecification<>(InterfaceComponent.class);
        Mockito.doReturn(interfaceComponentBean).when(componentManager)
                .getBean(Mockito.argThat(a -> a.getClasz().equals(InterfaceComponent.class)));

        BaseBean<TestClientComponent> bean = new BaseBean<>(
                TestClientComponent.class,
                null,
                Collections.emptyList(),
                false,
                (Constructor<TestClientComponent>) TestClientComponent.class.getDeclaredConstructors()[0],
                Arrays.asList(testDependencyComponentSpecification, testDependencyOtherComponentSpecification, interfaceComponentComponentSpecification),
                null
        );

        // WHEN
        TestClientComponent first = bean.construct(componentManager);
        TestClientComponent second = bean.construct(componentManager);

        // THEN
        Mockito.verify(dependencyBean, Mockito.times(2)).construct(Mockito.eq(componentManager));
        Mockito.verify(dependencyOtherBean, Mockito.times(2)).construct(Mockito.eq(componentManager));
        Mockito.verify(interfaceComponentBean, Mockito.times(2)).construct(Mockito.eq(componentManager));
        Assertions.assertNotSame(first, second);
    }
}
