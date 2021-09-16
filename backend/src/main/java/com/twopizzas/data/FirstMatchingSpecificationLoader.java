package com.twopizzas.data;

public class FirstMatchingSpecificationLoader<T extends Entity<ID>, ID, S extends Specification<T, CTX>, CTX> extends SpecificationExecutor<T, ID, S, CTX> implements ValueLoader<T> {
    public FirstMatchingSpecificationLoader(S specification, DataMapper<T, ID, S> dataMapper) {
        super(specification, dataMapper);
    }

    @Override
    public ValueHolder<T> load() {
        return BaseValueHolder.of(getOne());
    }
}