package com.twopizzas.data;

import java.util.Optional;

public interface IdentityMapper {
    <T extends Entity<ID>, ID> T testAndGet(T entity);
    <T extends Entity<ID>, ID> Optional<T> get(Class<T> clasz, ID id);
    <T extends Entity<ID>, ID> void markGone(T entity);
}
