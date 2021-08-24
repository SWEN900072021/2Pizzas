package com.twopizzas;

import com.twopizzas.di.Component;


public class MyHelloComponentImpl implements MyHelloComponent {
    public String getMessage() {
        return "hello from component";
    }
}
