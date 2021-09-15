package com.twopizzas.data;

import com.twopizzas.di.ThreadLocalComponent;

import java.util.*;

@ThreadLocalComponent
class GenericIdentityMapperImpl implements IdentityMapper {

    private Map<Object, Entity<?>> entities = new HashMap<>();
    private List<Object> goneIds = new ArrayList<>();

    @Override
    public <T extends Entity<ID>, ID>  T testAndGet(T entity) {
        assertNotGone(entity.getClass(), entity.getId());
        if (entities.containsKey(entity.getId())) {
            return (T) entities.get(entity.getId());
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <T extends Entity<ID>, ID>  Optional<T> get(Class<T> clasz, ID id) {
        assertNotGone(clasz, id);
        T entity = (T) entities.get(id);

        if (entity != null && clasz.isAssignableFrom(entity.getClass())) {
            return Optional.of(entity);
        }

        return Optional.empty();
    }

    @Override
    public <T extends Entity<ID>, ID>  void markGone(T entity) {
        if (!goneIds.contains(entity.getId())) {
            goneIds.add(entity.getId());
        }
    }

    @Override
    public void reset() {
        entities = new HashMap<>();
        goneIds = new ArrayList<>();
    }

    public void assertNotGone(Class<?> entity, Object id) {
        if (goneIds.contains(id)) {
            throw new DataConsistencyViolation(
                    String.format("attempted to get entity %s with is %s but entity has already been marked 'gone'",
                            entity.getName(),
                            id)
            );
        }
    }
}
