package com.twopizzas.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

interface Bean<T> extends ComponentConstructor<T> {

    Class<T> getClasz();

    String getQualifier();

    List<String> getProfiles();

    Constructor<T> getConstructor();

    Method getPostConstruct();

    boolean isPrimary();

    List<ComponentSpecification<?>> getDependencies();
}
