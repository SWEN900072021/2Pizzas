package com.twopizzas.data;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.ThreadLocalComponent;

import java.util.ArrayList;
import java.util.Collection;

@ThreadLocalComponent
public class UnitOfWorkImpl implements UnitOfWork {

    private final DataSource dataSource;
    private final DataMapperRegistry dataMapperRegistry;
    private Collection<Entity<?>> newEntities = new ArrayList<>();
    private Collection<Entity<?>> cleanEntities = new ArrayList<>();
    private Collection<Entity<?>> dirtyEntities = new ArrayList<>();
    private Collection<Entity<?>> removedEntities = new ArrayList<>();

    @Autowired
    UnitOfWorkImpl(DataSource dataSource, DataMapperRegistry dataMapperRegistry) {
        this.dataSource = dataSource;
        this.dataMapperRegistry = dataMapperRegistry;
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
        if (newEntities.isEmpty() && dirtyEntities.isEmpty() && removedEntities.isEmpty()) {
            // nothing to do!
            return;
        }

        // open a new db transaction
        dataSource.startNewTransaction();
        // apply each action
        newEntities.forEach(this::doCreate);
        dirtyEntities.forEach(this::doUpdate);
        removedEntities.forEach(this::doDelete);
        // commit
        dataSource.commitTransaction();

        // reset for next commit
        newEntities = new ArrayList<>();
        cleanEntities = new ArrayList<>();
        dirtyEntities = new ArrayList<>();
        removedEntities = new ArrayList<>();
    }

    private void assertTrueOrThrow(boolean shouldBeTrue, String message) {
        if (!shouldBeTrue) {
            throw new DataConsistencyViolation(message);
        }
    }

    private <T extends Entity<ID>, ID> DataMapper<T, ID, Specification<T, ?>> getMapper(T entity) {
        return dataMapperRegistry.getForClass(entity.getClass());
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
