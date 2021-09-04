package com.twopizzas.domain;

public class Customer extends User {
    private String givenName;
    private String lastName;
    private String email;

    public Customer(EntityId id, String username, String password, String userType, String givenName, String lastName, String email) {
        super(id, username, password, userType);
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
