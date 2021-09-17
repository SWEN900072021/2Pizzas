package com.twopizzas.api.user;

import com.twopizzas.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(source = "userType", target = "type")
    UserDto map(User user);
}
