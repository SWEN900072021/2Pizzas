package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, EntityId> {
    Optional<User> find(EntityId id);
}
