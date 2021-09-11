package com.twopizzas.di.othertestroot;

import com.twopizzas.di.Autowired;

public class MultipleAutowires {

    @Autowired
    MultipleAutowires() {}

    @Autowired
    MultipleAutowires(Object o) {}
}
