package com.twopizzas.port.data;

import com.twopizzas.data.*;

import java.lang.reflect.Proxy;
import java.util.List;

public abstract class AbstractSqlSpecification<T extends Entity<?>> implements Specification<T>, LazyValueHolderProxy.ValueLoader<List<T>> {
    protected final AbstractSqlDataMapper<T, ?, ?> dataMapper;

    protected abstract String getTemplate();
    protected abstract Object[] getTemplateValues();

    protected AbstractSqlSpecification(AbstractSqlDataMapper<T, ?, ?> dataMapper) {
        this.dataMapper = dataMapper;
    }

    @Override
    public List<T> execute() {
        return dataMapper.mapResultSet(dataMapper.doQuery(getTemplate(), getTemplateValues()));
    }

    @Override
    public ValueHolder<List<T>> load() {
        return new BaseValueHolder<>(execute());
    }

    public ValueHolder<List<T>> toLazy() {
        LazyValueHolderProxy<List<T>> proxy = new LazyValueHolderProxy<>(this);
        return (ValueHolder<List<T>>) Proxy.newProxyInstance(this.getClass().getClassLoader(), this.getClass().getInterfaces(), proxy);
    }
}
