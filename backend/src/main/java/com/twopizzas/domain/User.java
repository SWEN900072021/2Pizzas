package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

public abstract class User extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;
    private String username;
    private String password;

    public User(EntityId id, String username, String password) {
        notNull(id, "id");
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
