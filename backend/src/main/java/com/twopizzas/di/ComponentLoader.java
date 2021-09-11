package com.twopizzas.di;

import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentLoader implements BeanLoader {
    private final static Class<? extends Annotation> constructorTargetAnnotation = Autowired.class;
    private final String root;
    private final Reflections reflections;
    private List<ComponentConstructionInterceptor> constructionInterceptors;

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

    @Override
    public Collection<Bean<?>> load() {
        constructionInterceptors = discoverInterceptors();
        Collection<Class<?>> components = scanAllComponents();
        return components.stream().map(this::loadBean).collect(Collectors.toSet());
    }

    // package private for testing
    List<ComponentConstructionInterceptor> discoverInterceptors() {
        return reflections.getSubTypesOf(ComponentConstructionInterceptor.class).stream()
                .filter(this::isConcreteClass)
                .map(this::constructInterceptor)
                .collect(Collectors.toList());
    }

    private ComponentConstructionInterceptor constructInterceptor(Class<? extends ComponentConstructionInterceptor> clasz) {
        try {
            Constructor<? extends ComponentConstructionInterceptor> constructor = clasz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ApplicationContextException(String.format("failed to construct instance of %s %s, %s does not have a default constructor, component interceptors must have a default no argument constructor: %s",
                    ComponentConstructionInterceptor.class.getName(), clasz.getName(), clasz.getName(), e.getMessage()), e);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new ApplicationContextException(String.format("failed to construct instance of %s %s, invocation of default constructor failed: %s",
                    ComponentConstructionInterceptor.class.getName(), clasz.getName(), e.getMessage()), e);
        }
    }

    // package private for testing
    <T> Bean<T> loadBean(Class<T> clasz) throws LoadBeanException {
        String qualifier = getQualifier(clasz);
        List<String> profiles = getProfiles(clasz);
        boolean primary = isPrimary(clasz);
        Constructor<T> constructor = getAutowiredConstructor(clasz);
        List<TypedComponentSpecification<?>> dependencies = getDependencies(constructor);

        dependencies.forEach(d -> {
            if (!d.getClasz().isInterface()) {
                throw new LoadBeanException(clasz, String.format(
                        "dependency %s for component %s is not an interface, component dependencies must be interfaces", d.getClasz().getName(), clasz.getName()));
            }
        });

        Method postConstructor = getPostConstructor(clasz);
        boolean threadLocal = isThreadLocal(clasz);

        ComponentScope scope = getScope(clasz);
        Bean<T> baseBean = new BaseBean<>(clasz,
                qualifier,
                profiles,
                primary,
                constructor,
                dependencies,
                postConstructor,
                constructionInterceptors);

        if (threadLocal) {
            baseBean = new ThreadLocalBeanProxy<>(baseBean);
        }

        if (scope == ComponentScope.SINGLETON) {
            baseBean = new SingletonBeanProxy<>(baseBean);
        }

        return baseBean;
    }

    // package private for testing
    Collection<Class<?>> scanAllComponents() {
        return reflections.getTypesAnnotatedWith(Component.class).stream()
                .filter(this::isConcreteClass)
                .collect(Collectors.toList());
    }

    private List<TypedComponentSpecification<?>>  getDependencies(Constructor<?> constructor) {
        List<TypedComponentSpecification<?>> dependencies = new ArrayList<>();
        for (Parameter parameterType : constructor.getParameters()) {
            dependencies.add(scanParameter(parameterType));
        }
        return dependencies;
    }


    private TypedComponentSpecification<?> scanParameter(Parameter parameter) {
        BaseBeanSpecification<?> specification = new BaseBeanSpecification<>(parameter.getType());
        String qualifier = getQualifier(parameter);
        if (qualifier != null) {
            return new QualifiedBeanSpecification<>(qualifier, specification);
        }
        return specification;
    }

    private <T> Constructor<T> getAutowiredConstructor(Class<T> clasz) throws LoadBeanException {
        List<Constructor<?>> constructors = Arrays.stream(clasz.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(constructorTargetAnnotation))
                .collect(Collectors.toList());
        if (constructors.size() >  1)  {
            throw new LoadBeanException(clasz, String.format("multiple constructors annotated with %s, " +
                            "components must only declare one constructor annotated with %s",
                    constructorTargetAnnotation.getName(), constructorTargetAnnotation.getName()));
        }

        if (constructors.size() == 0) {
            try {
                return clasz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new LoadBeanException(clasz,String.format("no default constructor found. " +
                        "components must either declare one constructor annotated with %s or have a default constructor",
                        constructorTargetAnnotation.getName()));
            }
        }

        return (Constructor<T>) constructors.get(0);
    }

    private Method getPostConstructor(Class<?> clasz) throws LoadBeanException {
        List<Method> postConstructMethods = Arrays.stream(clasz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(PostConstruct.class) && m.getParameterTypes().length == 0)
                .collect(Collectors.toList());

        if (postConstructMethods.size() == 1) {
            return postConstructMethods.get(0);
        }

        return null;
    }

    private boolean isPrimary(Class<?> clasz) {
        Primary annotation = clasz.getAnnotation(Primary.class);
        return annotation != null;
    }

    private boolean isThreadLocal(Class<?> clasz) {
        ThreadLocalComponent annotation = clasz.getAnnotation(ThreadLocalComponent.class);
        return annotation != null;
    }

    private List<String> getProfiles(Class<?> clasz) {
        Profile annotation = clasz.getAnnotation(Profile.class);
        if (annotation == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(annotation.value());
    }

    private ComponentScope getScope(Class<?> clasz) {
        Scope annotation = clasz.getAnnotation(Scope.class);
        if (annotation == null) {
            return ComponentScope.SINGLETON;
        }
        return annotation.value();
    }

    private String getQualifier(Class<?> clasz) {
        Component annotation = clasz.getAnnotation(Component.class);
        if (annotation != null && !annotation.value().trim().equals("")) {
            return annotation.value();
        }
        return null;
    }

    private String getQualifier(Parameter parameter) {
        Qualifier annotation = parameter.getAnnotation(Qualifier.class);
        if (annotation != null && !annotation.value().trim().equals("")) {
            return annotation.value();
        }
        return null;
    }

    private boolean isConcreteClass(Class<?> clasz) {
        return !Modifier.isInterface(clasz.getModifiers()) && !Modifier.isAbstract(clasz.getModifiers());
    }
}
