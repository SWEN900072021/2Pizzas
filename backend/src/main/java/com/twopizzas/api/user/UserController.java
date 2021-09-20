package com.twopizzas.api.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.user.Administrator;
import com.twopizzas.domain.user.User;
import com.twopizzas.domain.user.UserRepository;
import com.twopizzas.web.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(
            path = "/user",
            method = HttpMethod.GET
    )
    @Authenticated(Administrator.TYPE)
    public RestResponse<List<UserDto>> getAllUsers() {
        return RestResponse.ok(userRepository.allUsers().stream().map(MAPPER::map).collect(Collectors.toList()));
    }

    @RequestMapping(
            path = "/admin",
            method = HttpMethod.POST
    )
    @Authenticated(Administrator.TYPE)
    public RestResponse<UserDto> createAdmin(@RequestBody NewAdminDto body) throws HttpException {
        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }
        User newAdministrator = userRepository.save(new Administrator(
                body.getUsername(),
                body.getPassword()
        ));
        return RestResponse.ok(MAPPER.map(newAdministrator));
    }
}
