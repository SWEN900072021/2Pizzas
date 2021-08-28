package com.twopizzas.data;

public class CreateEntity<T extends Entity<ID>, ID> implements LedgerEntry<T, ID> {
    private final T entity;

    CreateEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public void execute(MapperRegistry registry) {
        DataMapper<T, ID, ?> dataMapper = registry.getForClass(entity.getClass());
        dataMapper.create(entity);
    }
}
