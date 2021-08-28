package com.twopizzas.data;

import java.util.ArrayList;
import java.util.Collection;

public class UnitOfWorkImpl implements UnitOfWork {

    private final DataContext dataContext;
    private final Collection<Entity<?>> newEntities = new ArrayList<>();
    private final Collection<Entity<?>> cleanEntities = new ArrayList<>();
    private final Collection<Entity<?>> dirtyEntities = new ArrayList<>();
    private final Collection<Entity<?>> removedEntities = new ArrayList<>();

    UnitOfWorkImpl(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    @Override
    public <T extends Entity<ID>, ID> void registerNew(T entity) {
        assertTrueOrThrow(entity.getId() != null, "entity id must not be null");
        assertTrueOrThrow(!dirtyEntities.contains(entity), "attempted to register entity as new but entity already registered as dirty");
        assertTrueOrThrow(!removedEntities.contains(entity), "attempt to register entity as new but entity already registered as removed");
        assertTrueOrThrow(!newEntities.contains(entity), "attempt to register entity as new but entity already registered as new");
        newEntities.add(entity);
    }

    @Override
    public <T extends Entity<ID>, ID> void registerDirty(T entity) {
        assertTrueOrThrow(entity.getId() != null, "entity id must not be null");
        assertTrueOrThrow(!removedEntities.contains(entity), "attempt to register entity as new but entity already registered as removed");
        if (!newEntities.contains(entity) && !dirtyEntities.contains(entity)) {
            dirtyEntities.add(entity);
        }
    }

    @Override
    public <T extends Entity<ID>, ID> void registerClean(T entity) {
        // do nothing for now
    }

    @Override
    public <T extends Entity<ID>, ID> void registerDeleted(T entity) {
        assertTrueOrThrow(entity.getId() != null, "entity id must not be null");
        if (newEntities.remove(entity)) {
            return;
        }
        dirtyEntities.remove(entity);
        if (!removedEntities.contains(entity)) {
            removedEntities.add(entity);
        }
    }

    @Override
    public void commit() {
        // open a new db transaction
        dataContext.getDataSource().startNewTransaction();
        // apply each action
        newEntities.forEach(this::doCreate);
        dirtyEntities.forEach(this::doUpdate);
        removedEntities.forEach(this::doDelete);
        // commit
        dataContext.getDataSource().commitTransaction();
    }

    private void assertTrueOrThrow(boolean shouldBeTrue, String message) {
        if (!shouldBeTrue) {
            throw new DataConsistencyViolation(message);
        }
    }

    private <T extends Entity<ID>, ID> DataMapper<T, ID, Specification<T>> getMapper(T entity) {
        return dataContext.getMapperRegistry().getForClass(entity.getClass());
    }

    private <T extends Entity<ID>, ID> void doUpdate(T entity) {
        getMapper(entity).update(entity);
    }

    private <T extends Entity<ID>, ID> void doCreate(T entity) {
        getMapper(entity).create(entity);
    }

    private <T extends Entity<ID>, ID> void doDelete(T entity) {
        getMapper(entity).delete(entity);
    }
}
