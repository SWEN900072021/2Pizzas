package com.twopizzas.data;

import java.util.List;

public interface DataMapper<T extends Entity<ID>, ID, S extends Specification<T>> {
    void create(T entity);
    T read(ID id);
    List<T> readAll(S specification);
    void update(T entity);
    void delete(T entity);
}
