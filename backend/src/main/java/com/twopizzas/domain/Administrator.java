package com.twopizzas.domain;

public class Administrator extends User {

    public static final String TYPE = "Administrator";

    public Administrator(EntityId id, String username, String password) {
        super(id, username, password, TYPE);
    }

    public Administrator(String username, String password) {
        super(EntityId.nextId(), username, password, TYPE);
    }
}
