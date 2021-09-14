package com.twopizzas.port.data.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.user.User;
import com.twopizzas.domain.user.UserRepository;
import com.twopizzas.port.data.AbstractRepository;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl extends AbstractRepository<User, UserSpecification, UserMapper> implements UserRepository {

    @Autowired
    public UserRepositoryImpl(UserMapper dataMapper) {
        super(dataMapper);
    }

    public Optional<User> find(String username, String password) {
        List<User> users = doSpecification(new UserByUsernameAndPasswordSpecification(dataMapper, username, password));
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }
}
