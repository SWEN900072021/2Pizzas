package com.twopizzas.data;

public interface UnitOfWork {
    <T extends Entity<ID>, ID> void registerNew(T entity);
    <T extends Entity<ID>, ID> void registerDirty(T entity);
    <T extends Entity<ID>, ID> void registerClean(T entity);
    <T extends Entity<ID>, ID> void registerDeleted(T entity);
    void commit();
}