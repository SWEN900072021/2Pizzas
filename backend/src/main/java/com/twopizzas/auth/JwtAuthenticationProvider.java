package com.twopizzas.auth;

import com.twopizzas.configuration.Configuration;
import com.twopizzas.configuration.Value;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.User;
import com.twopizzas.domain.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

@Component
@Configuration
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Value("authentication.secret")
    private String secret;
    private SecretKey key;

    @Value("authentication.token.seconds.to.live")
    private String timeToLive;

    @Value("authentication.issuer")
    private String issuer;

    private final UserRepository userRepository;

    JwtAuthenticationProvider(String secret, String timeToLive, String issuer, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.secret = secret;
        this.timeToLive = timeToLive;
        this.issuer = issuer;
    }

    @Autowired
    JwtAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> authenticate(String token) {

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            if (!jws.getBody().getAudience().equals(issuer)) {
                return Optional.empty();
            }

            String userId = jws.getBody().getId();
            return userRepository.find(EntityId.of(userId));

        } catch (JwtException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> login(String username, String password) {

        Optional<User> maybeUser = userRepository.find(username, password);
        return maybeUser.map(this::login);

    }

    @Override
    public String login(User user) {
        Date now = new Date();
        Calendar gcal = new GregorianCalendar();
        gcal.setTime(now);
        gcal.add(Calendar.SECOND, Integer.parseInt(timeToLive));
        Date expires = gcal.getTime();

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject("2-pizzas-api")
                .setAudience(issuer)
                .setExpiration(expires)
                .setNotBefore(now)
                .setIssuedAt(now)
                .setId(user.getId().toString())
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }
}
