package com.twopizzas.di;

import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentLoader {
    private final String root;
    private final Reflections reflections;

    ComponentLoader(String root) {
        this.root = root;
        reflections = new Reflections(root, new ConfigurationBuilder()
                .addScanners(new MethodParameterScanner(),
                        new MethodAnnotationsScanner(),
                        new TypeAnnotationsScanner(),
                        new SubTypesScanner(),
                        new FieldAnnotationsScanner()
                )
        );
    }

    Collection<Class<?>> scanAllComponents() {
        return reflections.getTypesAnnotatedWith(Component.class).stream().filter(
                this::isConcreteClass
        ).collect(Collectors.toList());
    }

    List<Class<?>>  getDependencies(Class<?> clasz) {
        Constructor<?> constructor = getAutowiredConstructor(clasz);
        List<Class<?>> dependencies = new ArrayList<>();
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            dependencies.add(getConcreteComponent(parameterType));
        }
        return dependencies;
    }

    <T> T construct(Class<T> clasz, List<?> args) throws ComponentInstantiationException {
        T instance;
        try {
            Constructor<T> constructor = getAutowiredConstructor(clasz);
            constructor.setAccessible(true);
            instance = constructor.newInstance(args.toArray());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ComponentInstantiationException(String.format(
                    "failed to construct component %s call: %s",
                    clasz.getName(),
                    e.getMessage()
            ));
        }

        // look for post construct method and invoke if found
        List<Method> postConstructMethods = Arrays.stream(clasz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Autowired.class) && m.getParameterTypes().length == 0)
                .collect(Collectors.toList());
        if (postConstructMethods.size() > 1) {
            throw new ComponentInstantiationException(String.format("multiple methods annotated with %s declared for component %s, " +
                    "components must only declare one method annotated with %s", PostConstruct.class.getName(), clasz.getName(), PostConstruct.class.getName()));
        }

        if (postConstructMethods.size() == 1) {
            Method postConstruct = postConstructMethods.get(0);
            postConstruct.setAccessible(true); // maybe a little naughty here
            try {
                postConstruct.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ComponentInstantiationException(String.format(
                        "error invoking %s annotated method %s on component %s: %s",
                        PostConstruct.class.getName(),
                        postConstruct.getName(),
                        clasz.getName(),
                        e.getMessage())
                );
            }
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> getAutowiredConstructor(Class<T> clasz) {
        List<Constructor<?>> constructors = Arrays.stream(clasz.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList());
        if (constructors.size() >  1)  {
            throw new ApplicationContextException(String.format("multiple constructors annotated with %s for class %s, " +
                            "components must only declare one constructor annotated with %s",
                    Autowired.class.getName(), clasz.getName(), Autowired.class.getName()));
        }

        if (constructors.size() == 0) {
            try {
                return clasz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new ApplicationContextException(String.format("no default constructor found for component %s, " +
                        "components must either declare one constructor annotated with %s or have a default constructor",
                        clasz.getName(), Autowired.class.getName()));
            }
        }

        return (Constructor<T>) constructors.get(0);
    }

    <T> Class<? extends T> getConcreteComponent(Class<T> clasz) throws ApplicationContextException {
        // the class is a concrete component we don't need to go hunting for implementations or subTypes, just return
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clasz);
        if (isConcreteClass(clasz) && isComponent(clasz) && subTypes.isEmpty()) {
            return clasz;
        }

        // class is not concrete or not a component, find component implementations
        List<Class<? extends T>> implementations = subTypes.stream()
                .filter(this::isConcreteClass)
                .filter(this::isComponent)
                .collect(Collectors.toList());
        if (implementations.size() > 1) {
            // multiple implementations not supported
            // TODO: implement @Qualifier
            throw new ComponentNotFound(
                    String.format("multiple implementations for %s found", clasz.getName()));
        }

        if (implementations.size() == 0) {
            throw new ComponentNotFound(
                    String.format("no concrete implementation annotated with %s found for %s",
                            Component.class.getName(), clasz.getName()));
        }

        return implementations.get(0);
    }

    private boolean isComponent(Class<?> clasz) {
        return clasz.getAnnotation(Component.class) != null;
    }

    private boolean isConcreteClass(Class<?> clasz) {
        return !Modifier.isInterface(clasz.getModifiers()) && !Modifier.isAbstract(clasz.getModifiers());
    }
}
