package com.twopizzas.di;

import com.twopizzas.di.testroot.InterfaceComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;

public class ThreadLocalBeanProxyTests {

    @Test
    @DisplayName("WHEN construct invoked THEN returns proxied instance")
    void test() {
        // GIVEN
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);
        Bean<StubComponent> baseBean = Mockito.mock(Bean.class);
        Bean<StubComponent> bean = new ThreadLocalBeanProxy<>(baseBean);

        Mockito.when(baseBean.construct(Mockito.any())).thenAnswer(ars -> new StubComponent());
        Mockito.when(baseBean.getClasz()).thenReturn(StubComponent.class);

        // WHEN
        InterfaceComponent instance = bean.construct(componentManager);

        // THEN
        Assertions.assertTrue(Proxy.isProxyClass(instance.getClass()));
        Assertions.assertEquals(ThreadLocalProxy.class, Proxy.getInvocationHandler(instance).getClass());
    }

    @Test
    @DisplayName("WHEN multiple calls to construct THEN returns new instance each time")
    void test2() {
        // GIVEN
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);
        Bean<StubComponent> baseBean = Mockito.mock(Bean.class);
        Bean<StubComponent> bean = new ThreadLocalBeanProxy<>(baseBean);

        Mockito.when(baseBean.construct(Mockito.any())).thenAnswer(ars -> new StubComponent());
        Mockito.when(baseBean.getClasz()).thenReturn(StubComponent.class);

        // WHEN
        InterfaceComponent first = bean.construct(componentManager);
        InterfaceComponent second = bean.construct(componentManager);

        // THEN
        Assertions.assertNotSame(first, second);
        Mockito.verify(baseBean, Mockito.never()).construct(Mockito.any());
    }

    static class StubComponent implements InterfaceComponent {
        public String getString() {
            return "hello";
        }
    }
}
