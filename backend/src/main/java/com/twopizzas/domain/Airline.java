package com.twopizzas.domain;

public class Airline extends User {

    private String name;

    private String code;

    public Airline(EntityId id, String username, String password) {
        super(id, username, password);
    }

    public Airline(EntityId id, String username, String password, String name, String code) {
        super(id, username, password);
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
