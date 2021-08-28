package com.twopizzas.data;

public class DeleteEntity<T extends Entity<ID>, ID> implements LedgerEntry<T, ID> {
    private final T entity;

    DeleteEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public void execute(MapperRegistry registry) {
        DataMapper<T, ID, ?> dataMapper = registry.getForClass(entity.getClass());
        dataMapper.update(entity);
    }
}
