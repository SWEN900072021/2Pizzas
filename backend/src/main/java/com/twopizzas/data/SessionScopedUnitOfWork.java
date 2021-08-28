package com.twopizzas.data;

import java.util.ArrayList;
import java.util.List;

public class SessionScopedUnitOfWork implements UnitOfWork {

    private DataContext dataContext;

    SessionScopedUnitOfWork(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    List<LedgerEntry<? extends Entity<?>, ?>> ledger;

    SessionScopedUnitOfWork() {
        this.ledger = new ArrayList<>();
    }

    @Override
    public void registerNew(Entity<?> entity) {
        ledger.add(new CreateEntity<>(entity));
    }

    @Override
    public void registerDirty(Entity<?> entity) {
        ledger.add(new UpdateEntity<>(entity));
    }

    @Override
    public void registerClean(Entity<?> entity) {
        // no op for now
    }

    @Override
    public void registerDeleted(Entity<?> entity) {
        ledger.add(new DeleteEntity<>(entity));
    }

    @Override
    public void commit() {
        // open a new db transaction
        dataContext.getDataSource().startNewTransaction();
        // apply each action
        ledger.forEach(e -> e.execute(dataContext.getMapperRegistry()));
        // commit
        dataContext.getDataSource().commitTransaction();
    }
}
