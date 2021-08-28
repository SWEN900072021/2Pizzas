package com.twopizzas.data;

public class UpdateEntity<T extends Entity<ID>, ID> implements LedgerEntry<T, ID> {
    private final T entity;

    UpdateEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public void execute(MapperRegistry registry) {
        DataMapper<T, ID, ?> dataMapper = registry.getForClass(entity.getClass());
        dataMapper.update(entity);
    }
}