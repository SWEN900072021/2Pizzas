package com.twopizzas;

import com.twopizzas.di.Component;
import com.twopizzas.di.Primary;
import com.twopizzas.di.Profile;

@Component
@Profile("coolGuy")
public class MyHelloComponentImplButBetter implements MyHelloComponent {
    public String getMessage() {
        return "isn't that better?";
    }
}