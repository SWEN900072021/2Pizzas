package com.twopizzas.data;

import com.twopizzas.util.AssertionConcern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataProxy<T extends Entity<ID>, ID> extends AssertionConcern implements InvocationHandler {
    private final DataContext dataContext;
    private final DataMapper<T, ID, ?> dataMapper;

    DataProxy(DataMapper<T, ID, ?> dataMapper, DataContext dataContext) {
        this.dataMapper = dataMapper;
        this.dataContext = dataContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "create":
                return handleCreate(args);
            case "read":
                return handleRead(method, args);
            case "readAll":
                return handleReadAll(method, args);
            case "update":
                return handleUpdate(args);
            case "delete":
                return handleDelete(args);
            default:
                return method.invoke(dataMapper, args);
        }
    }

    private Object handleRead(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        // check if it's in the map first
        IdentityMapper<T, ID> identityMapper = dataContext.getIdentityMapperRegistry().getForClass(dataMapper.getEntityClass());
        Optional<T> maybeInMap = identityMapper.get(dataMapper.getEntityClass(), (ID) args[0]);
        if (maybeInMap.isPresent()) {
            return maybeInMap.get();
        }

        T maybeFound = dataMapper.getEntityClass().cast(method.invoke(dataMapper, args));
        if (maybeFound != null) {
            Entity<ID> found = identityMapper.testAndGet(maybeFound);
            dataContext.getUnitOfWork().registerClean(found);
            return found;
        }
        return null;
    }

    private Object handleReadAll(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Collection<T> all = (Collection<T>) method.invoke(dataMapper, args);

        // switch out search results for what is in the map
        IdentityMapper<T, ID> identityMapper = dataContext.getIdentityMapperRegistry().getForClass(dataMapper.getEntityClass()) ;
        return all.stream().map(identityMapper::testAndGet).collect(Collectors.toList());
    }

    private Object handleCreate(Object[] args) {
        T toSave = dataMapper.getEntityClass().cast(args[0]);
        notNull(toSave, "entity");

        //method.invoke(dataMapper, args);
        dataContext.getUnitOfWork().registerNew(toSave);
        return dataContext.getIdentityMapperRegistry().getForClass(dataMapper.getEntityClass()).testAndGet(toSave);
    }

    private Object handleUpdate(Object[] args) {
        T toSave = dataMapper.getEntityClass().cast(args[0]);
        notNull(toSave, "entity");

        dataContext.getUnitOfWork().registerDirty(toSave);
        return toSave;
    }

    private Object handleDelete(Object[] args) {
        T toDelete = dataMapper.getEntityClass().cast(args[0]);
        notNull(toDelete, "entity");

        dataContext.getIdentityMapperRegistry().getForClass(dataMapper.getEntityClass()).markGone(toDelete);
        dataContext.getUnitOfWork().registerDeleted(toDelete);
        return null;
    }
}
