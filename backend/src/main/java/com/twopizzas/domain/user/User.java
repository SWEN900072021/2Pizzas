package com.twopizzas.domain.user;

import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;

@Getter
public abstract class User extends DomainEntity {
    private String username;
    private String password;

    public User(EntityId id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }

    public abstract String getUserType();
}
