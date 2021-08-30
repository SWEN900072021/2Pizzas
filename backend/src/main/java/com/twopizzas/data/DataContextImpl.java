package com.twopizzas.data;

import com.twopizzas.di.Component;

@Component
public class DataContextImpl implements DataContext {

    private final ThreadLocal<UnitOfWork> unitOfWorkThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<IdentityMapperRegistry> identityMapperRegistryThreadLocal = new ThreadLocal<>();

    @Override
    public UnitOfWork getUnitOfWork() {
        if (unitOfWorkThreadLocal.get() == null ) {
            unitOfWorkThreadLocal.set(new UnitOfWorkImpl(this));
        }
        return unitOfWorkThreadLocal.get();
    }

    @Override
    public IdentityMapperRegistry getIdentityMapperRegistry() {
        if (identityMapperRegistryThreadLocal.get() == null ) {
            //identityMapperRegistryThreadLocal.set(new UnitOfWorkImpl(this));
        }
        return identityMapperRegistryThreadLocal.get();
    }

    @Override
    public DataMapperRegistry getDataMapperRegistry() {
        return null;
    }

    @Override
    public DataSource getDataSource() {
        return null;
    }
}
