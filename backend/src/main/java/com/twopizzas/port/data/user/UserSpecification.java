package com.twopizzas.port.data.user;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.db.ConnectionPool;

public interface UserSpecification extends Specification<User, ConnectionPool> {
}