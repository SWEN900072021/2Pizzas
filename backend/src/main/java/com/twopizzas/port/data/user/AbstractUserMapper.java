package com.twopizzas.port.data.user;

import com.twopizzas.domain.User;

public abstract class AbstractUserMapper<T extends User> {

    public void abstractCreate(T entity) {
        INSERT into user (id, username, password, type) entity.getId()
    }

    public void abstractUpdate(T entity) {
        entity.
    }

    public void abstractDelete(T entity) {

    }
}
