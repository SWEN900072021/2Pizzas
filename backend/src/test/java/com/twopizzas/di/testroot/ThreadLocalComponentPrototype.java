package com.twopizzas.di.testroot;

import com.twopizzas.di.ComponentScope;
import com.twopizzas.di.Scope;
import com.twopizzas.di.ThreadLocalComponent;

@ThreadLocalComponent
@Scope(ComponentScope.PROTOTYPE)
public class ThreadLocalComponentPrototype {
}
