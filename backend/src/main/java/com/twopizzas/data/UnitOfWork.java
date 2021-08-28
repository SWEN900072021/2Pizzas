package com.twopizzas.data;

public interface UnitOfWork {
    void registerNew(Entity<?> entity);
    void registerDirty(Entity<?> entity);
    void registerClean(Entity<?> entity);
    void registerDeleted(Entity<?> entity);
    void commit();
}
