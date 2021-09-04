package com.twopizzas.domain;

public class Customer extends User {
    private String givenName;
    private String lastName;
    private String email;

    public Customer(EntityId id, String username, String password) {
        super(id, username, password);
    }

    public Customer(EntityId id, String username, String password, String givenName, String lastName, String email) {
        super(id, username, password);
        this.givenName = givenName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
