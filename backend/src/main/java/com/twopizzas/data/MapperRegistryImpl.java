package com.twopizzas.data;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistryImpl implements MapperRegistry {

    private final Map<Class<? extends Entity<?>>, DataMapper<? extends Entity<?>, ?, ?>> mappers = new HashMap<>();

    @Override
    public <T extends Entity<ID>, ID, U extends DataMapper<T, ID, S>, S extends Specification<T>> U getForClass(Class<T> clasz) {
        if (mappers.containsKey(clasz)) {
            return (U) mappers.get(clasz);
        }
        throw new MapperNotFound(clasz);
    }

    @Override
    public <T extends Entity<ID>, ID, U extends DataMapper<T, ID, S>, S extends Specification<T>> void register(Class<T> clasz, U dataMapper) {
        mappers.put(clasz, dataMapper);
    }

    class MapperNotFound extends RuntimeException {
        MapperNotFound(Class<?> clasz) {
            super(String.format("no mapper registered for class %s", clasz.getName()));
        }
    }
}
