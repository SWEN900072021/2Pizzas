package com.twopizzas.domain;

public class Airline extends User {

    public static final String TYPE = "airline";
    private final String name;

    private final String code;

    public Airline(EntityId id, String username, String password, String name, String code) {
        super(id, username, password, TYPE);
        this.name = name;
        this.code = code;
    }

    public Airline(String username, String password, String name, String code) {
        this(EntityId.nextId(), username, password, name, code);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
