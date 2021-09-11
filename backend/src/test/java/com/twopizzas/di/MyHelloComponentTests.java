package com.twopizzas.di;

import com.twopizzas.MyHelloComponentImpl;
import com.twopizzas.MyHelloComponentImplButBetter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MyHelloComponentTests {

    @Test
    @DisplayName("get rid pf me!!! ;)")
    void test() {
        new MyHelloComponentImpl().getMessage();
        new MyHelloComponentImplButBetter().getMessage();
    }
}
