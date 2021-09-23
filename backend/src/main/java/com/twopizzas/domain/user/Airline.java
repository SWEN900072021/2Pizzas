package com.twopizzas.domain.user;

import com.twopizzas.domain.EntityId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Airline extends User {

    public static final String TYPE = "airline";
    private final String name;
    private final String code;

    public Airline(EntityId id, String username, String password, String name, String code, Status status) {
        super(id, username, password, status);
        this.name = name;
        this.code = code;
    }

    public Airline(String username, String password, String name, String code) {
        this(EntityId.nextId(), username, password, name, code, Status.ACTIVE);
    }

    @Override
    public String getUserType() {
        return TYPE;
    }
}
