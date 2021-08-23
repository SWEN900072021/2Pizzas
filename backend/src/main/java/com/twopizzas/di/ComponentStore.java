package com.twopizzas.di;

import java.util.*;
import java.util.stream.Collectors;

class ComponentStore {

    // protected for testing
    protected Set<Object> store = new HashSet<>();

    <T> T get(Class<T> clasz) throws ApplicationContextException {
        List<Object> components = store.stream()
                .filter(e -> clasz.isAssignableFrom(e.getClass()))
                .collect(Collectors.toList());

        if (components.size() > 1) {
            throw new DuplicateComponentException(String.format("multiple components found for class %s", clasz.getName()));
        }

        if (components.size() == 1) {
            return unsafeCast(components.get(0));
        }

        return null;
    }

    public void register(Object object) {
        Object current = get(object.getClass());
        if (current != null) {
            throw new DuplicateComponentException(String.format("register component %s failed, duplicate component %s already registered",
                    object.getClass(), current.getClass()));
        }

        store.add(object);
    }

    Set<Object> getAll() {
        return new HashSet<>(store);
    }

    @SuppressWarnings({"unchecked"})
    private <T> T unsafeCast(Object o) {
        return (T) o;
    }
}
