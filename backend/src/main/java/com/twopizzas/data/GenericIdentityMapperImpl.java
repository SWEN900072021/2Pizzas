package com.twopizzas.data;

import java.util.*;

public class GenericIdentityMapperImpl<T extends Entity<ID>, ID> implements IdentityMapper<T, ID> {

    private Map<ID, T> entities = new HashMap<>();
    private List<ID> goneIds = new ArrayList<>();
    private final Class<T> clasz;

    GenericIdentityMapperImpl(Class<T> clasz) {
        this.clasz = clasz;
    }

    @Override
    public T testAndGet(T entity) {
        assertNotGone(entity.getClass(), entity.getId());
        if (entities.containsKey(entity.getId())) {
            return entities.get(entity.getId());
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> get(Class<T> clasz, ID id) {
        assertNotGone(clasz, id);
        T entity = entities.get(id);

        if (entity != null && clasz.isAssignableFrom(entity.getClass())) {
            return Optional.of(entity);
        }

        return Optional.empty();
    }

    @Override
    public void markGone(T entity) {
        if (!goneIds.contains(entity.getId())) {
            goneIds.add(entity.getId());
        }
    }

    @Override
    public IdentityMapper<T, ID> forClass(Class<T> clasz) {
        return null;
    }

    @Override
    public void reset() {
        entities = new HashMap<>();
        goneIds = new ArrayList<>();
    }

    public void assertNotGone(Class<?> entity, ID id) {
        if (goneIds.contains(id)) {
            throw new DataConsistencyViolation(
                    String.format("attempted to get entity %s %s which has been marked 'gone'",
                            entity.getName(),
                            id)
            );
        }
    }
}
