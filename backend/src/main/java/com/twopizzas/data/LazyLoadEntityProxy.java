package com.twopizzas.data;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LazyLoadEntityProxy<T extends Entity<ID>, ID> extends TransparentEntityProxy<T> {
    private final MapperRegistry mapperRegistry;
    private boolean loaded;


    LazyLoadEntityProxy(T entity, MapperRegistry mapperRegistry) {
        super(entity);
        this.mapperRegistry = mapperRegistry;
    }

    private void load() {
        DataMapper<T, ID, ?> dataMapper = mapperRegistry.getForClass(entity.getClass());
        T loadedEntity = dataMapper.read(entity.getId());
        try {
            BeanUtils.copyProperties(entity, loadedEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("failed to lazy load entity %s copy properties failed, error: %s", entity, e.getMessage()));
        }
    }

    @Override
    public Object invokeTransparent(Object proxy, Method method, Object[] args) throws Throwable {
        if (!loaded) {
            load();
        }
        return method.invoke(entity, args);
    }
}
