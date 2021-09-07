package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
