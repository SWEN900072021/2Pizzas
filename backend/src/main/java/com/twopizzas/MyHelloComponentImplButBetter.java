package com.twopizzas;

import com.twopizzas.di.Component;

@Component
public class MyHelloComponentImplButBetter implements MyHelloComponent {
    public String getMessage() {
        return "isn't that better?";
    }
}