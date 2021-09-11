package com.twopizzas.domain;

public class Customer extends User {

    public static final String TYPE = "customer";
    private String givenName;
    private String lastName;
    private String email;

    public Customer(EntityId id, String username, String password, String givenName, String lastName, String email) {
        super(id, username, password, TYPE);
        this.givenName = givenName;
        this.lastName = lastName;
        this.email = email;
    }

    public Customer(String username, String password, String givenName, String lastName, String email) {
        super(EntityId.nextId(), username, password, TYPE);
        this.givenName = givenName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

}
