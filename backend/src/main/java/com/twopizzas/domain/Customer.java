package com.twopizzas.domain;

import java.util.Objects;

public class Customer extends User {
    private String givenName;
    private String lastName;
    private String email;

    public Customer(EntityId id, String username, String password, String givenName, String lastName, String email) {
        super(id, username, password);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(givenName, customer.givenName) && Objects.equals(lastName, customer.lastName) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), givenName, lastName, email);
    }
}
