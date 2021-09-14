package com.twopizzas.api;

import com.twopizzas.auth.AuthenticationProvider;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.User;
import com.twopizzas.domain.user.UserRepository;
import com.twopizzas.web.HttpMethod;
import com.twopizzas.web.RequestBody;
import com.twopizzas.web.RequestMapping;
import com.twopizzas.web.RestResponse;

import java.util.Optional;

@Controller
public class UserController {
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public UserController(UserRepository userRepository, AuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
    }

    @RequestMapping(path = "/signup", method = HttpMethod.POST)
    RestResponse<SignupResponseDTO> createUser(@RequestBody SignupRequestDTO body) {
        User user = new Customer(body.getUsername(), body.getPassword(), body.getGivenName(), body.getSurname(), body.getEmail());
        userRepository.save(user);
        //String token = authenticationProvider.login(user.getUsername(), user.getPassword()).orElseThrow(() -> new RuntimeException("Couldn't get token"));
        return RestResponse.ok(new SignupResponseDTO());
    }

}
