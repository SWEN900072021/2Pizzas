package com.twopizzas.di.othertestroot;

import com.twopizzas.di.PostConstruct;

public class MultiplePostConstruct {
    @PostConstruct
    void init() {}

    @PostConstruct
    void init(Object o) {}
}
