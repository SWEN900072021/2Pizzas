package com.twopizzas.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

class TransparentEntityProxyTests {

    @Test
    @DisplayName("GIVEN entity wrapped in proxy WHEN equals invoked on unwrapped entity THEN returns true")
    void test() {
        // GIVEN
        StubEntity entity = new StubEntity(UUID.randomUUID().toString());
        TransparentEntityProxy<StubEntity> transparentEntityProxy = new StubTransparentEntityProxy(entity);

        Object o = Proxy.newProxyInstance(StubEntity.class.getClassLoader(), new Class[]{Entity.class}, transparentEntityProxy);
        StubEntity wrapped = (StubEntity) o;
         
        // WHEN
        boolean wrappedEqualsEntity = wrapped.equals(entity);

        // THEN
        Assertions.assertTrue(wrappedEqualsEntity);
    }

    @Test
    @DisplayName("GIVEN entity wrapped in proxy WHEN hash invoked on unwrapped entity THEN returns true")
    void test2() {
        // GIVEN
        StubEntity entity = new StubEntity(UUID.randomUUID().toString());
        TransparentEntityProxy<StubEntity> transparentEntityProxy = new StubTransparentEntityProxy(entity);
        Entity wrapped = (Entity) Proxy.newProxyInstance(Entity.class.getClassLoader(), new Class[]{Entity.class}, transparentEntityProxy);

        // WHEN
        boolean entityEqualsWrapped = entity.equals(wrapped);
        boolean wrappedEqualsEntity = wrapped.equals(entity);

        // THEN
        Assertions.assertTrue(entityEqualsWrapped);
        Assertions.assertTrue(wrappedEqualsEntity);
    }

    static class StubTransparentEntityProxy extends TransparentEntityProxy<StubEntity> {

        StubTransparentEntityProxy(StubEntity entity) {
            super(entity);
        }

        @Override
        public Object invokeTransparent(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(entity, args);
        }
    }

    static class StubEntity implements Entity<String> {
        private final String id;

        StubEntity(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return null;
        }
    }
}
