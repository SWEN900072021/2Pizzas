package com.twopizzas.data;

import java.util.HashMap;
import java.util.Map;

public class DataMapperRegistryImpl implements DataMapperRegistry {

    private final Map<Class<? extends Entity<?>>, DataMapper<? extends Entity<?>, ?, ?>> mappers = new HashMap<>();

    @Override
    public <T extends Entity<ID>, ID, U extends DataMapper<T, ID, S>, S extends Specification<T>> U getForClass(Class<T> clasz) {
        if (mappers.containsKey(clasz) && mappers.get(clasz).getEntityClass().equals(clasz)) {
            return (U) mappers.get(clasz);
        }
        throw new MapperNotFound(clasz);
    }

    @Override
    public <T extends Entity<ID>, ID, U extends DataMapper<T, ID, S>, S extends Specification<T>> void register(Class<T> clasz, U dataMapper) {
        if (mappers.containsKey(clasz)) {
            throw new MapperRegistrationException(clasz, String.format("mapper %s already registered for class %s", mappers.get(clasz).getEntityClass(), clasz.getName()));
        }
        mappers.put(clasz, dataMapper);
    }

    static class MapperRegistrationException extends RuntimeException {
        MapperRegistrationException(Class<?> clasz, String message ) {
            super(String.format("failed to register mapper for class %s, error: %s", clasz.getName(), message));
        }
    }

    static class MapperNotFound extends RuntimeException {
        MapperNotFound(Class<?> clasz) {
            super(String.format("no mapper registered for class %s", clasz.getName()));
        }
    }
}
