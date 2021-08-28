package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;


class LazyValueHolderProxyTests {

    @Test
    @DisplayName("GIVEN proxy WHEN get invoked THEN loads value")
    void test() {
        // GIVEN
        String testValue = "stub";
        LazyValueHolderProxy.ValueLoader<String> loader = Mockito.mock(StubLoader.class);
        StubHolder holder = Mockito.mock(StubHolder.class);
        Mockito.when(holder.get()).thenReturn(testValue);
        Mockito.when(loader.load()).thenReturn(holder);

        LazyValueHolderProxy<String> proxy = new LazyValueHolderProxy<>(loader);
        ValueHolder<String> wrapped = (ValueHolder<String>) Proxy.newProxyInstance(
                LazyValueHolderProxy.class.getClassLoader(), new Class[]{ValueHolder.class}, proxy);

        // WHEN
        String value = wrapped.get();

        // THEN
        Mockito.verify(loader).load();
        Mockito.verify(holder).get();
        Assertions.assertEquals(testValue, value);
        Assertions.assertTrue(wrapped.isPresent());
    }

    @Test
    @DisplayName("GIVEN proxy WHEN get invoked multiple times THEN loads value only once")
    void test2() {
        // GIVEN
        String testValue = "stub";
        LazyValueHolderProxy.ValueLoader<String> loader = Mockito.mock(StubLoader.class);
        StubHolder holder = Mockito.mock(StubHolder.class);
        Mockito.when(holder.get()).thenReturn(testValue);
        Mockito.when(loader.load()).thenReturn(holder);

        LazyValueHolderProxy<String> proxy = new LazyValueHolderProxy<>(loader);
        ValueHolder<String> wrapped = (ValueHolder<String>) Proxy.newProxyInstance(
                LazyValueHolderProxy.class.getClassLoader(), new Class[]{ValueHolder.class}, proxy);

        // WHEN
        wrapped.get();
        wrapped.get();

        // THEN
        Mockito.verify(loader).load();
    }

    @Test
    @DisplayName("GIVEN proxy WHEN isPresent invoked prior to get THEN returns false")
    void test3() {
        // GIVEN
        String testValue = "stub";
        LazyValueHolderProxy.ValueLoader<String> loader = Mockito.mock(StubLoader.class);
        StubHolder holder = Mockito.mock(StubHolder.class);
        Mockito.when(holder.get()).thenReturn(testValue);
        Mockito.when(loader.load()).thenReturn(holder);

        LazyValueHolderProxy<String> proxy = new LazyValueHolderProxy<>(loader);
        ValueHolder<String> wrapped = (ValueHolder<String>) Proxy.newProxyInstance(
                LazyValueHolderProxy.class.getClassLoader(), new Class[]{ValueHolder.class}, proxy);

        // WHEN
        boolean isPresent = wrapped.isPresent();

        // THEN
        Assertions.assertFalse(isPresent);
    }

    static class StubHolder implements ValueHolder<String> {

        private final String value;

        StubHolder(String value) {
            this.value = value;
        }


        @Override
        public String get() {
            return value;
        }

        @Override
        public boolean isPresent() {
            return value != null;
        }
    }

    static class StubLoader implements LazyValueHolderProxy.ValueLoader<String> {
        @Override
        public ValueHolder<String> load() {
            return new StubHolder("stub");
        }
    }

}
