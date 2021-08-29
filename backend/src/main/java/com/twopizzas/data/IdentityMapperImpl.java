package com.twopizzas.data;

import java.util.*;

public class IdentityMapperImpl implements IdentityMapper {

    private final Map<Object, Entity<?>> entities = new HashMap<>();
    private final List<Object> goneIds = new ArrayList<>();

    @Override
    public <T extends Entity<ID>, ID> T testAndGet(T entity) {
        assertNotGone(entity.getClass(), entity.getId());
        if (entities.containsKey(entity.getId())) {
            return (T) entities.get(entity.getId());
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <T extends Entity<ID>, ID> Optional<T> get(Class<T> clasz, ID id) {
        assertNotGone(clasz, id);
        Entity<?> entity = entities.get(id);

        if (entity != null && clasz.isAssignableFrom(entity.getClass())) {
            return Optional.of((T) entity);
        }

        return Optional.empty();
    }

    @Override
    public <T extends Entity<ID>, ID> void markGone(T entity) {
        if (!goneIds.contains(entity.getId())) {
            goneIds.add(entity.getId());
        }
    }

    public <T extends Entity<ID>, ID> void assertNotGone(Class<T> entity, ID id) {
        if (goneIds.contains(id)) {
            throw new DataConsistencyViolation(
                    String.format("attempted to get entity %s %s which has been marked 'gone'",
                            entity.getName(),
                            id)
            );
        }
    }
}
