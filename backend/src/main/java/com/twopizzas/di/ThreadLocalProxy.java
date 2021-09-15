package com.twopizzas.di;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ThreadLocalProxy<T> implements InvocationHandler {
    private final ThreadLocal<T> local = new ThreadLocal<>();
    private final ComponentConstructor<T> componentConstructor;
    private final ComponentManager componentManager;

    public ThreadLocalProxy(ComponentConstructor<T> componentConstructor, ComponentManager componentManager) {
        this.componentConstructor = componentConstructor;
        this.componentManager = componentManager;
    }

    ThreadLocal<T> getLocal() {
        return local;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (local.get() == null) {
            local.set(componentConstructor.construct(componentManager));
        }

        try {
            return method.invoke(local.get(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
