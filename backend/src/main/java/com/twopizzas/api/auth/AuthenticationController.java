package com.twopizzas.api.auth;

import com.twopizzas.auth.AuthenticationProvider;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.UserRepository;
import com.twopizzas.web.*;

@Controller
public class AuthenticationController {
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public AuthenticationController(UserRepository userRepository, AuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
    }

    @RequestMapping(path = "/signup", method = HttpMethod.POST)
    RestResponse<SignupResponseDTO> createUser(@RequestBody SignupRequestDTO body) {
        Customer user = new Customer(body.getUsername(), body.getPassword(), body.getGivenName(), body.getSurname(), body.getEmail());
        userRepository.save(user);
        return RestResponse.ok(new SignupResponseDTO()
                .setId(user.getId().toString())
                .setEmail(user.getEmail())
                .setGivenName(user.getGivenName())
                .setSurname(user.getLastName()));
    }

    @RequestMapping(path = "/login", method = HttpMethod.POST)
    RestResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) throws HttpException {
        String token = authenticationProvider.login(body.getUsername(), body.getPassword()).orElseThrow(
                () -> new HttpException(HttpStatus.UNAUTHORIZED)
        );

        return RestResponse.ok(new LoginResponseDTO().setToken(token));
    }

}
