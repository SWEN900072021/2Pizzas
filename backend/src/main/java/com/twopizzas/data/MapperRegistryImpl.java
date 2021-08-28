package com.twopizzas.data;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistryImpl implements MapperRegistry {

    private final Map<Class<? extends Entity<?>>, DataMapper<? extends Entity<?>, ?, ?>> mappers = new HashMap<>();

    @Override
    public <ID, U extends Entity<ID>, T extends DataMapper<U, ID, ?>> T getForClass(Class<U> clasz) {
        if (mappers.containsKey(clasz)) {
            return (T) mappers.get(clasz);
        }
        throw new MapperNotFound(clasz);
    }

    @Override
    public <ID, U extends Entity<ID>, T extends DataMapper<U, ID, ?>> void register(Class<U> clasz, T dataMapper) {
        mappers.put(clasz, dataMapper);
    }

    class MapperNotFound extends RuntimeException {
        MapperNotFound(Class<?> clasz) {
            super(String.format("no mapper registered for class %s", clasz.getName()));
        }
    }
}
