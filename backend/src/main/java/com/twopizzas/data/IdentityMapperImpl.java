package com.twopizzas.data;

import java.util.Optional;

public class IdentityMapperImpl implements IdentityMapper {
    @Override
    public <T extends Entity<ID>, ID> T testAndGet(T entity) {
        return null;
    }

    @Override
    public <T extends Entity<ID>, ID> Optional<T> get(Class<T> clasz, ID id) {
        return Optional.empty();
    }

    @Override
    public <T extends Entity<ID>, ID> void markGone(T entity) {

    }
}
