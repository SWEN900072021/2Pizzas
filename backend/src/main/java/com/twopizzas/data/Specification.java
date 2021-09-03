package com.twopizzas.data;

import java.util.List;

public interface Specification<T extends Entity<?>, CTX> {
     List<T> execute(CTX context);
}
