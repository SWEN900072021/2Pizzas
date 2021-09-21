package com.twopizzas.domain.user;

import com.twopizzas.data.Repository;
import com.twopizzas.domain.EntityId;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, EntityId> {
    Optional<User> find(String username, String password);
    List<User> allUsers();
}
