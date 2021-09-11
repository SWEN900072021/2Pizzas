package com.twopizzas.data;

import java.util.List;

public class AllMatchingSpecificationLoader<T extends Entity<ID>, ID, S extends Specification<T, CTX>, CTX> extends SpecificationExecutor<T, ID, S, CTX> implements ValueLoader<List<T>> {
    public AllMatchingSpecificationLoader(S specification, DataMapper<T, ID, S> dataMapper) {
        super(specification, dataMapper);
    }

    @Override
    public ValueHolder<List<T>> load() {
        return BaseValueHolder.of(getAll());
    }
}
