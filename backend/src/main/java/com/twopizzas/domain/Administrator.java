package com.twopizzas.domain;

public class Administrator extends User {
    public Administrator(EntityId id, String username, String password) {
        super(id, username, password);
    }

    public Administrator(String username, String password) {
        super(EntityId.nextId(), username, password);
    }
}
