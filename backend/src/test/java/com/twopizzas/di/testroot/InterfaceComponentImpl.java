package com.twopizzas.di.testroot;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;

@Component
public class InterfaceComponentImpl implements InterfaceComponent {

    @Autowired
    public InterfaceComponentImpl() { }

    @Override
    public String getString() {
        return this.getClass().getCanonicalName();
    }
}
