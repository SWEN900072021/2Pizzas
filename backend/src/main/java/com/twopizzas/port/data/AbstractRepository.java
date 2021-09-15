package com.twopizzas.port.data;

import com.twopizzas.data.DataMapper;
import com.twopizzas.data.Repository;
import com.twopizzas.data.Specification;
import com.twopizzas.domain.EntityId;

import java.util.List;
import java.util.Optional;

public class AbstractRepository<T extends DomainEntity, S extends Specification<T, ?>, U extends DataMapper<T, EntityId, S>>  implements Repository<T, EntityId> {

    protected final U dataMapper;

    public AbstractRepository(U dataMapper) {
        this.dataMapper = dataMapper;
    }

    @Override
    public Optional<T> find(EntityId id) {
        T maybeEntity = dataMapper.read(id);
        if (maybeEntity != null) {
            maybeEntity.setNew(false);
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
        entity.setNew(false);
        return entity;
    }

    protected List<T> doSpecification(S specification) {
        List<T> entities = dataMapper.readAll(specification);
        entities.forEach(e -> e.setNew(false));
        return entities;
    }

    @Override
    public void remove(T entity) {
        dataMapper.delete(entity);
    }
}
