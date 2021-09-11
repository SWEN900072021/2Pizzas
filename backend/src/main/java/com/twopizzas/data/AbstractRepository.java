package com.twopizzas.data;

import java.util.Optional;

public class AbstractRepository<T extends Entity<ID>, ID, S extends Specification<T, ?>, U extends DataMapper<T, ID, S>>  implements Repository<T, ID> {

    protected final U dataMapper;

    public AbstractRepository(U dataMapper) {
        this.dataMapper = dataMapper;
    }

    @Override
    public Optional<T> find(ID id) {
        T maybeEntity = dataMapper.read(id);
        if (maybeEntity != null) {
            return Optional.of(maybeEntity);
        }
        return Optional.empty();
    }

    @Override
    public T save(T entity) {
        if (entity.isNew()) {
            dataMapper.create(entity);
        } else {
            dataMapper.update(entity);
        }
        return entity;
    }

    @Override
    public void remove(T entity) {
        dataMapper.delete(entity);
    }
}
