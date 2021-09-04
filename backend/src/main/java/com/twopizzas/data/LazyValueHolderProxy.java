package com.twopizzas.data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LazyValueHolderProxy<T> implements InvocationHandler {
    private ValueHolder<T> wrapped;
    private final ValueLoader<T> valueLoader;

    public LazyValueHolderProxy(ValueLoader<T> valueLoader) {
        this.valueLoader = valueLoader;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "get":
                return handleGet(proxy, method, args);
            case "isPresent":
                return handleIsPresent(proxy, method, args);
            default:
                if (wrapped == null) {
                    wrapped = valueLoader.load();
                }
                return method.invoke(wrapped, args);
        }
    }

    public Object handleGet(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (wrapped == null) {
            wrapped = valueLoader.load();
        }
        return method.invoke(wrapped, args);
    }

    public Object handleIsPresent(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (wrapped == null) {
            return false;
        }
        return method.invoke(wrapped, args);
    }

    public static <T> ValueHolder<T> makeLazy(ValueLoader<T> loader) {
        LazyValueHolderProxy<T> proxy = new LazyValueHolderProxy<>(loader);
        return (ValueHolder<T>) Proxy.newProxyInstance(ValueHolder.class.getClassLoader(), new Class<?>[]{ValueLoader.class}, proxy);
    }
}
