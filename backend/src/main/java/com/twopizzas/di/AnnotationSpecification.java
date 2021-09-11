package com.twopizzas.di;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Collectors;

public class AnnotationSpecification implements ComponentSpecification {

    private final Class<? extends Annotation> annotation;

    public AnnotationSpecification(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public String describe() {
        return String.format("components with annotation %s", annotation.getName());
    }

    @Override
    public Collection<Bean<?>> filter(Collection<Bean<?>> beans) {
        return beans.stream().filter(b -> b.getClasz().getAnnotation(annotation) != null).collect(Collectors.toList());
    }
}
