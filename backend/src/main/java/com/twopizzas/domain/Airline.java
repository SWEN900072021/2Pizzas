package com.twopizzas.domain;

public class Airline extends User {

    private String name;

    private String code;

    public Airline(EntityId id, String username, String password, String userType) {
        super(id, username, password, userType);
    }

    public Airline(EntityId id, String username, String password, String userType, String name, String code) {
        super(id, username, password, userType);
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
