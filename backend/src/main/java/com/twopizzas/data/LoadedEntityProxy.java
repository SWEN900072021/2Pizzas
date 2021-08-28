package com.twopizzas.data;

import java.lang.reflect.Method;

class LoadedEntityProxy extends TransparentEntityProxy<Entity<?>> {

    LoadedEntityProxy(Entity<?> entity) {
        super(entity);
    }

    @Override
    public Object invokeTransparent(Object proxy, Method method, Object[] args) throws Throwable {
        if ("isNew".equals(method.getName())) {
            return handleIsNew(proxy, method, args);
        }
        return method.invoke(entity, args);
    }

    public Object handleIsNew(Object proxy, Method method, Object[] args) {
        return false;
    }
}
