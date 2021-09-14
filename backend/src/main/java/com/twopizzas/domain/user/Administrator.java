package com.twopizzas.domain.user;

import com.twopizzas.domain.EntityId;

public class Administrator extends User {

    public static final String TYPE = "administrator";

    public Administrator(EntityId id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getUserType() {
        return TYPE;
    }

    public Administrator(String username, String password) {
        super(EntityId.nextId(), username, password);
    }
}
