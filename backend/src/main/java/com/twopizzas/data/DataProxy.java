package com.twopizzas.data;

import com.twopizzas.util.AssertionConcern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

class DataProxy<T extends Entity<ID>, ID> extends AssertionConcern implements InvocationHandler {
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
                return invoke(method, args);
        }
    }

    private Object handleRead(Method method, Object[] args) throws Throwable {
        // check if it's in the map first
        Optional<T> maybeInMap = identityMapper.get(dataMapper.getEntityClass(), (ID) args[0]);
        if (maybeInMap.isPresent()) {
            return maybeInMap.get();
        }

        T maybeFound = dataMapper.getEntityClass().cast(invoke(method, args));
        if (maybeFound != null) {
            Entity<ID> found = identityMapper.testAndGet(maybeFound);
            unitOfWork.registerClean(found);
            return found;
        }
        return null;
    }

    private Object handleReadAll(Method method, Object[] args) throws Throwable {
        Collection<T> all = (Collection<T>) invoke(method, args);

        // switch out search results for what is in the map
        return all.stream().map(identityMapper::testAndGet).collect(Collectors.toList());
    }

    private Object handleCreate(Object[] args) {
        T toSave = dataMapper.getEntityClass().cast(args[0]);
        notNull(toSave, "entity");

        T inMapper = identityMapper.testAndGet(toSave);
        unitOfWork.registerNew(inMapper);
        return null;
    }

    private Object handleUpdate(Object[] args) {
        T toSave = dataMapper.getEntityClass().cast(args[0]);
        notNull(toSave, "entity");
        T inMapper = identityMapper.testAndGet(toSave);
        if (inMapper != toSave) {
            throw new DataConsistencyViolation(String.format("call to update entity %s %s with unknown object, entity already exists in identity mapper and objects are not the same", toSave.getClass().getName(), toSave.getId()));
        }

        unitOfWork.registerDirty(inMapper);
        return null;
    }

    private Object handleDelete(Object[] args) {
        T toDelete = dataMapper.getEntityClass().cast(args[0]);
        notNull(toDelete, "entity");

        T inMapper = identityMapper.testAndGet(toDelete);
        identityMapper.markGone(inMapper);
        unitOfWork.registerDeleted(inMapper);
        return null;
    }

    private Object invoke(Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(dataMapper, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
