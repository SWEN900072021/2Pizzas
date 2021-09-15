package com.twopizzas.di;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LazyComponentProxy<T> implements InvocationHandler {
    private final ComponentManager componentManager;
    private final Bean<T> bean;
    private T wrapped;

    // package private for unit testing
    T getWrapped() {
        return wrapped;
    }

    public LazyComponentProxy(ComponentManager componentManager, Bean<T> bean) {
        this.componentManager = componentManager;
        this.bean = bean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (wrapped == null) {
            wrapped = bean.construct(componentManager);
        }

        try {
            return method.invoke(wrapped, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
