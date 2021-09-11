package com.twopizzas.di;

import com.twopizzas.di.testroot.InterfaceComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;
import java.util.concurrent.*;

public class ThreadLocalComponentTests {

    @Test
    @DisplayName("GIVEN proxied instance WHEN any method invoked THEN constructed thread local instance is invoked")
    void test() {
        // GIVEN
        StubComponent component = Mockito.mock(StubComponent.class);
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);
        Bean<StubComponent> baseBean = Mockito.mock(Bean.class);

        Mockito.when(baseBean.construct(Mockito.any())).thenAnswer(ars -> component);
        Mockito.when(baseBean.getClasz()).thenReturn(StubComponent.class);
        Mockito.when(component.getString()).thenReturn("someString");

        ThreadLocalProxy<StubComponent> proxy = new ThreadLocalProxy<>(baseBean, componentManager);
        InterfaceComponent instance = (InterfaceComponent) Proxy.newProxyInstance(StubComponent.class.getClassLoader(), StubComponent.class.getInterfaces(), proxy);

        // WHEN
        String string = instance.getString();

        // THEN
        Mockito.verify(baseBean).construct(Mockito.eq(componentManager));
        Mockito.verify(component).getString();
    }

    @Test
    @DisplayName("GIVEN multiple threads WHEN any method invoked THEN instance constructed for each thread and invoked")
    void test2() throws ExecutionException, InterruptedException {
        // GIVEN
        ComponentManager componentManager = Mockito.mock(ComponentManager.class);
        Bean<StubComponent> baseBean = Mockito.mock(Bean.class);

        Mockito.when(baseBean.construct(Mockito.any())).thenAnswer(ars -> new StubComponent());
        Mockito.when(baseBean.getClasz()).thenReturn(StubComponent.class);

        ThreadLocalProxy<StubComponent> proxy = new ThreadLocalProxy<>(baseBean, componentManager);
        InterfaceComponent instance = (InterfaceComponent) Proxy.newProxyInstance(StubComponent.class.getClassLoader(), StubComponent.class.getInterfaces(), proxy);

        // WHEN
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> firstFuture = executorService.submit(() -> instance.getString());
        Future<String> secondFuture = executorService.submit(() -> instance.getString());

        String first = firstFuture.get();
        String second = secondFuture.get();

        // THEN
        Mockito.verify(baseBean, Mockito.times(2)).construct(Mockito.eq(componentManager));
        Assertions.assertEquals("0", first);
        Assertions.assertEquals("0", second);
    }

    static class StubComponent implements InterfaceComponent {
        private int value = 0;
        public String getString() {
            int v = value;
            value++;
            return Integer.toString(v);
        }
    }
}
