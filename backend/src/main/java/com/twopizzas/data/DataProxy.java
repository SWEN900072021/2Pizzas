package com.twopizzas.data;

import com.twopizzas.util.AssertionConcern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataProxy<T extends Entity<ID>, ID> extends AssertionConcern implements InvocationHandler {
    private final DataContext dataContext;
    private final DataMapper<T, ID, ? extends Specification<T>> dataMapper;
    private final Class<T> clasz;

    DataProxy(Class<T> clasz, DataMapper<T, ID, ? extends Specification<T>> dataMapper, DataContext dataContext) {
        this.dataMapper = dataMapper;
        this.clasz = clasz;
        this.dataContext = dataContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "create":
                return handleCreate(proxy, method, args);
            case "read":
                return handleRead(proxy, method, args);
            case "readAll":
                return handleReadAll(proxy, method, args);
            case "update":
                return handleUpdate(proxy, method, args);
            case "delete":
                return handleDelete(proxy, method, args);
            default:
                return method.invoke(dataMapper, args);
        }
    }

    private Object handleRead(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        // check if it's in the map first
        Optional<T> maybeInMap = dataContext.getIdentityMapper().get(clasz, (ID) args[0]);
        if (maybeInMap.isPresent()) {
            return maybeInMap.get();
        }

        T maybeFound = (T) method.invoke(dataMapper, args);
        if (maybeFound != null) {
            Entity<ID> found = dataContext.getIdentityMapper().testAndGet(maybeFound);
            dataContext.getUnitOfWork().registerClean(found);
            return found;
        }
        return null;
    }

    private Object handleReadAll(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Collection<T> all = (Collection<T>) method.invoke(dataMapper, args);

        // switch out search results for what is in the map
        return all.stream().map(dataContext.getIdentityMapper()::testAndGet).collect(Collectors.toList());
    }

    private Object handleCreate(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        T toSave = (T) args[0];
        notNull(toSave, "entity");

        //method.invoke(dataMapper, args);
        dataContext.getUnitOfWork().registerNew(toSave);
        return dataContext.getIdentityMapper().testAndGet(toSave);
    }

    private Object handleUpdate(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        T toSave = (T) args[0];
        notNull(toSave, "entity");

        dataContext.getUnitOfWork().registerDirty(toSave);
        return toSave;
    }

    private Object handleDelete(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        T toDelete = (T) args[0];
        notNull(toDelete, "entity");

        dataContext.getIdentityMapper().markGone(toDelete);
        dataContext.getUnitOfWork().registerDeleted(toDelete);
        return null;
    }
}
