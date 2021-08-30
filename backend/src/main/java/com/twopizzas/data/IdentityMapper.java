package com.twopizzas.data;

import java.util.Optional;

public interface IdentityMapper<T extends Entity<ID>, ID> {
    T testAndGet(T entity);
    Optional<T> get(Class<T> clasz, ID id);
    void markGone(T entity);
    IdentityMapper<T, ID> forClass(Class<T> clasz);
    void reset();
}
