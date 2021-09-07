package com.twopizzas.domain;

import java.util.Objects;

public class Airline extends User {

    private String name;

    private String code;

    public Airline(EntityId id, String username, String password, String name, String code) {
        super(id, username, password);
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Airline airline = (Airline) o;
        return Objects.equals(name, airline.name) && Objects.equals(code, airline.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, code);
    }
}
