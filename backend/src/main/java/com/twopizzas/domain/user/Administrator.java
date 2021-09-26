package com.twopizzas.domain.user;

import com.twopizzas.domain.EntityId;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Administrator extends User {

    public static final String TYPE = "administrator";

    public Administrator(EntityId id, String username, String password, UserStatus status) {
        super(id, username, password, status);
    }

    @Override
    public String getUserType() {
        return TYPE;
    }

    public Administrator(String username, String password) {
        super(EntityId.nextId(), username, password, UserStatus.ACTIVE);
    }
}
