package com.twopizzas.di;

import java.util.Collection;

interface BeanLoader {
    Collection<Bean<?>> load();
}
