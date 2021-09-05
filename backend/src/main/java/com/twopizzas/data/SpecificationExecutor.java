package com.twopizzas.data;

import java.util.List;

public abstract class SpecificationExecutor<T extends Entity<ID>, ID, S extends Specification<T, CTX>, CTX> {
    private final S specification;
    private final DataMapper<T, ID, S> dataMapper;

    protected SpecificationExecutor(S specification, DataMapper<T, ID, S> dataMapper) {
        this.specification = specification;
        this.dataMapper = dataMapper;
    }

    protected List<T> getAll() {
        return dataMapper.readAll(specification);
    }

    protected T getOne() {
        return dataMapper.readAll(specification).stream().findFirst().orElse(null);
    }
}
