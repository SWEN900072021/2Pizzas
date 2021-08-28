package com.twopizzas.data;

import java.util.Optional;

public interface Repository<T extends Entity<ID>, ID> {
    Optional<T> find(ID id);
    T save(T entity);
    void remove(T entity);
}
