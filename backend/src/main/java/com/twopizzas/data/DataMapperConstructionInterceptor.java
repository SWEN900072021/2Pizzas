package com.twopizzas.data;

import com.twopizzas.di.ComponentConstructionInterceptor;
import com.twopizzas.di.ComponentManager;

import java.lang.reflect.Proxy;

public class DataMapperConstructionInterceptor implements ComponentConstructionInterceptor {
    DataMapperRegistry registry;
    UnitOfWork unitOfWork;
    IdentityMapper identityMapper;

    @Override
    public <T> T intercept(T component, ComponentManager componentManager) {

        if (!(component instanceof DataMapper)) {
            return component;
        }

        fetchComponents(componentManager);

        DataMapper<? extends Entity<?>, ?, ? extends Specification<?, ?>> mapper = (DataMapper<? extends Entity<?>, ?, ? extends Specification<?, ?>>) component;
        mapper.register(registry);

        DataProxy<? extends Entity<?>, ?> proxy = new DataProxy<>(mapper, identityMapper, unitOfWork);
        return (T) Proxy.newProxyInstance(mapper.getClass().getClassLoader(), mapper.getClass().getInterfaces(), proxy);
    }

    private void fetchComponents(ComponentManager componentManager) {
        if (registry == null) {
            registry = componentManager.getComponent(DataMapperRegistry.class);
        }

        if (unitOfWork == null) {
            unitOfWork = componentManager.getComponent(UnitOfWork.class);
        }

        if (identityMapper == null) {
            identityMapper = componentManager.getComponent(IdentityMapper.class);
        }
    }
}
