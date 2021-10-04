package com.twopizzas.api.auth;

import com.twopizzas.web.AuthenticationProvider;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.User;
import com.twopizzas.domain.user.UserRepository;
import com.twopizzas.web.*;

@Controller
public class AuthenticationController {
    private final UserRepository repository;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public AuthenticationController(UserRepository userRepository, AuthenticationProvider authenticationProvider) {
        this.repository = userRepository;
        this.authenticationProvider = authenticationProvider;
    }

    @RequestMapping(path = "/signup", method = HttpMethod.POST)
    RestResponse<SignupResponseDto> createUser(@RequestBody SignupRequestDto body) throws HttpException {
        Customer user = new Customer(body.getUsername(), body.getPassword(), body.getGivenName(), body.getSurname(), body.getEmail());
        repository.save(user);

        String token = authenticationProvider.login(user);

        return RestResponse.ok(new SignupResponseDto()
                .setId(user.getId().toString())
                .setEmail(user.getEmail())
                .setGivenName(user.getGivenName())
                .setSurname(user.getLastName())
                .setToken(token));
    }

    @RequestMapping(path = "/login", method = HttpMethod.POST)
    RestResponse<LoginResponseDto> login(@RequestBody LoginRequestDto body) throws HttpException {
        String token = authenticationProvider.login(body.getUsername(), body.getPassword()).orElseThrow(
                () -> new HttpException(HttpStatus.UNAUTHORIZED)
        );

        User user = repository.find(body.getUsername(), body.getPassword()).orElseThrow(() -> new HttpException((HttpStatus.NOT_FOUND)));
        return RestResponse.ok(new LoginResponseDto().setToken(token).setUsername(user.getUsername()).setUserType(user.getUserType()));
    }
}
