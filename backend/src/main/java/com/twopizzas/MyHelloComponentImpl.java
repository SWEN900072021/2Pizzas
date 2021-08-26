package com.twopizzas;

import com.twopizzas.di.Component;
import com.twopizzas.di.Primary;

@Primary
@Component
public class MyHelloComponentImpl implements MyHelloComponent {
    public String getMessage() {
        return "hello from component";
    }
}
