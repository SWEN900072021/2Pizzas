package com.twopizzas.domain.user;

import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DomainEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class User extends DomainEntity {
    private final String username;
    private final String password;

    @Setter
    private UserStatus status;

    public User(EntityId id, String username, String password, UserStatus status) {
        super(id);
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public User(EntityId id, String username, String password) {
        this(id, username, password, UserStatus.ACTIVE);
    }

    public abstract String getUserType();

    public enum UserStatus {
        ACTIVE,
        INACTIVE
    }
}
