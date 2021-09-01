package com.twopizzas.data;

import com.twopizzas.util.AssertionConcern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataProxy<T extends Entity<ID>, ID> extends AssertionConcern implements InvocationHandler {
    private final IdentityMapper identityMapper;
    private final DataMapper<T, ID, ?> dataMapper;
    private final UnitOfWork unitOfWork;

    DataProxy(DataMapper<T, ID, ?> dataMapper, IdentityMapper identityMapper, UnitOfWork unitOfWork) {
        this.dataMapper = dataMapper;
        this.identityMapper = identityMapper;
        this.unitOfWork = unitOfWork;
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
        Optional<T> maybeInMap = identityMapper.get(dataMapper.getEntityClass(), (ID) args[0]);
        if (maybeInMap.isPresent()) {
            return maybeInMap.get();
        }

        T maybeFound = dataMapper.getEntityClass().cast(method.invoke(dataMapper, args));
        if (maybeFound != null) {
            Entity<ID> found = identityMapper.testAndGet(maybeFound);
            unitOfWork.registerClean(found);
            return found;
        }
        return null;
    }

    private Object handleReadAll(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Collection<T> all = (Collection<T>) method.invoke(dataMapper, args);

        // switch out search results for what is in the map
        return all.stream().map(identityMapper::testAndGet).collect(Collectors.toList());
    }

    private Object handleCreate(Object[] args) {
        T toSave = dataMapper.getEntityClass().cast(args[0]);
        notNull(toSave, "entity");

        unitOfWork.registerNew(toSave);
        return identityMapper.testAndGet(toSave);
    }

    private Object handleUpdate(Object[] args) {
        T toSave = dataMapper.getEntityClass().cast(args[0]);
        notNull(toSave, "entity");
        T inMapper = identityMapper.testAndGet(toSave);
        if (inMapper != toSave) {
            throw new DataConsistencyViolation(String.format("call to update entity %s %s with unknown object, entity already exists in identity mapper and objects are not the same", toSave.getClass().getName(), toSave.getId()));
        }

        unitOfWork.registerDirty(toSave);
        return toSave;
    }

    private Object handleDelete(Object[] args) {
        T toDelete = dataMapper.getEntityClass().cast(args[0]);
        notNull(toDelete, "entity");

        identityMapper.markGone(toDelete);
        unitOfWork.registerDeleted(toDelete);
        return null;
    }
}
