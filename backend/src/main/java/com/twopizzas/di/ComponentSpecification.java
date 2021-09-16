package com.twopizzas.di;

import java.util.Collection;

public interface ComponentSpecification {
    String describe();
    Collection<Bean<?>> filter(Collection<Bean<?>> beans);
}
