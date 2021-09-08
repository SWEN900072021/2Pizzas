package com.twopizzas.port.data.user;

import com.twopizzas.data.DataMapper;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.User;
import com.twopizzas.port.data.SqlResultSetMapper;

public interface UserMapper extends DataMapper<User, EntityId, UserSpecification>, SqlResultSetMapper<User> {
    @Override
    default Class<User> getEntityClass() {
        return User.class;
    }
}