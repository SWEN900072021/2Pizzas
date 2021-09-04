package com.twopizzas.domain;

public class Administrator extends User {

    public Administrator(EntityId id, String username, String password, String userType) {
        super(id, username, password, userType);
    }
}
