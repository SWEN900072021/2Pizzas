package com.twopizzas.auth;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.User;
import com.twopizzas.domain.user.UserRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class JwtAuthenticationProviderTests {

    @Mock
    private UserRepository userRepository;

    AuthenticationProvider authenticationProvider;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        authenticationProvider = new JwtAuthenticationProvider(
                "3YA5ID3IhT6IqGSco2H8la+fMqzfkclN",
                "3600",
                "http://some.place.com",
                userRepository
        );
    }

    @Test
    @DisplayName("GIVEN user exists WHEN login THEN returns jwt")
    void test() {
        // GIVEN
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(EntityId.nextId());
        Mockito.when(user.getStatus()).thenReturn(User.Status.ACTIVE);
        Mockito.when(userRepository.find(Mockito.any(), Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.find(Mockito.any())).thenReturn(Optional.of(user));
        String username = "username";
        String password = "password";

        // WHEN
        Optional<String> token = authenticationProvider.login(username, password);

        // THEN
        Assertions.assertTrue(token.isPresent());
        Mockito.verify(userRepository).find(Mockito.eq(username), Mockito.eq(password));

        Optional<User> authenticatedUser = authenticationProvider.authenticate(token.get());
        Mockito.verify(userRepository).find(Mockito.eq(user.getId()));
        Assertions.assertTrue(authenticatedUser.isPresent());
        Assertions.assertSame(user, authenticatedUser.get());
    }

    @Test
    @DisplayName("GIVEN token signed by different secret WHEN authenticate THEN returns empty")
    void test2() {
        // GIVEN
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(EntityId.nextId());
        Mockito.when(user.getStatus()).thenReturn(User.Status.ACTIVE);
        Mockito.when(userRepository.find(Mockito.any(), Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.find(Mockito.any())).thenReturn(Optional.of(user));
        String username = "username";
        String password = "password";

        AuthenticationProvider authenticationProviderOther = new JwtAuthenticationProvider(
                Keys.secretKeyFor(SignatureAlgorithm.HS256).toString(),
                "3600",
                "http://some.place.com",
                userRepository
        );

        Optional<String> token = authenticationProviderOther.login(username, password);
        Assertions.assertTrue(token.isPresent());
        
        // WHEN
        Optional<User> authenticatedUser = authenticationProvider.authenticate(token.get());

        // THEN
        Assertions.assertFalse(authenticatedUser.isPresent());
    }
}
