package com.twopizzas.auth;

import com.twopizzas.domain.user.User;

import java.util.Optional;

public interface AuthenticationProvider {
    Optional<User> authenticate(String token);
    Optional<String> login(String username, String password);
    String login(User user);
}
