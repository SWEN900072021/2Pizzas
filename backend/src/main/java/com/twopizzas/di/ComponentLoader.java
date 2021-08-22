package com.twopizzas.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class ComponentLoader {
    private final String root;

    ComponentLoader(String root) {
        this.root = root;
    }

    Collection<Class<?>> scanAllComponents() {
        return null;
    }

    List<Class<?>>  getDependencies(Class<?> clasz) {
        return null;
    }

    <T> T instantiate(Class<T> clasz, List<?> args) throws ComponentInstantiationError {
        Class<?>[] argClasses = new Class[args.size()];
        for (int i = 0; i < args.size(); i++) {
            argClasses[i] = args.get(i).getClass();
        }

        try {
            Constructor<T> constructor = clasz.getConstructor(argClasses);
            return constructor.newInstance(args.toArray());
        } catch (NoSuchMethodException e) {
            throw new ComponentInstantiationError("unable to create component, the requiest");
        } catch (InvocationTargetException e) {
            throw new ComponentInstantiationError("unable to create component, the requiest");
        } catch (InstantiationException e) {
            throw new ComponentInstantiationError("unable to create component, the requiest");
        } catch (IllegalAccessException e) {
            throw new ComponentInstantiationError("unable to create component, the requiest");
        }
    }
}
