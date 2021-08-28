package com.twopizzas.data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class TransparentEntityProxy<T extends Entity<?>> implements InvocationHandler {
    protected final T entity;

    public abstract Object invokeTransparent(Object proxy, Method method, Object[] args) throws Throwable;

    TransparentEntityProxy(T entity) {
        this.entity = entity;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "equals":
                return handleEquals(proxy, method, args);
            case "hashCode":
                return handleHashCode(proxy, method, args);
            default:
                return method.invoke(entity, args);
        }
    }

    public Object handleEquals(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return unwrapAndInvoke(method, args);
    }

    public Object handleHashCode(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return unwrapAndInvoke(method, args);
    }

    private Object unwrapAndInvoke(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (args[0] instanceof TransparentEntityProxy) {
            return method.invoke(entity, ((TransparentEntityProxy<?>) args[0]).entity);
        }
        return method.invoke(entity, args);
    }
}
