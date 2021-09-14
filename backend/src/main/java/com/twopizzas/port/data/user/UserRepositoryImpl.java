package com.twopizzas.port.data.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.AbstractRepository;

import java.util.List;

@Component
public class UserRepositoryImpl extends AbstractRepository<User, UserSpecification, UserMapper> {

    @Autowired
    public UserRepositoryImpl(UserMapper dataMapper) {
        super(dataMapper);
    }

    List<User> find(String username, String password) {
        return doSpecification(new UserByUsernameAndPasswordSpecification(dataMapper, username, password));
    }
}
