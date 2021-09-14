package com.twopizzas.domain.user;

import com.twopizzas.domain.EntityId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Customer extends User {

    public static final String TYPE = "customer";
    private String givenName;
    private String lastName;
    private String email;

    public Customer(EntityId id, String username, String password, String givenName, String lastName, String email) {
        super(id, username, password);
        this.givenName = givenName;
        this.lastName = lastName;
        this.email = email;
    }

    public Customer(String username, String password, String givenName, String lastName, String email) {
        super(EntityId.nextId(), username, password);
        this.givenName = givenName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String getUserType() {
        return TYPE;
    }
}
