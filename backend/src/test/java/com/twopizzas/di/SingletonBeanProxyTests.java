package com.twopizzas.di;

import com.twopizzas.di.testroot.TestDependency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SingletonBeanProxyTests {

    @Test
    @DisplayName("WHEN multiple calls to construct THEN returns same instance")
    void test() {
        // GIVEN
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);
        Bean<TestDependency> baseBean = Mockito.mock(Bean.class);
        Bean<TestDependency> bean = new SingletonBeanProxy<>(baseBean);

        Mockito.when(baseBean.construct(Mockito.any())).thenReturn(new TestDependency());

        // WHEN
        TestDependency first = bean.construct(componentManager);
        TestDependency second = bean.construct(componentManager);

        // THEN
        Mockito.verify(baseBean).construct(Mockito.eq(componentManager));
        Assertions.assertSame(first, second);
    }
}
