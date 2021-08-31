package com.twopizzas.data;

import java.util.List;

public interface DataMapper<T extends Entity<ID>, ID, S extends Specification<T>> {
    T create(T entity);
    T read(ID id);
    List<T> readAll(S specification);
    T update(T entity);
    void delete(T entity);
    Class<T> getEntityClass();
}
