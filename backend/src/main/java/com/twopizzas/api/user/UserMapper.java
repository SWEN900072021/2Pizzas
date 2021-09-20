package com.twopizzas.api.user;

import com.twopizzas.api.BaseMapper;
import com.twopizzas.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = BaseMapper.class)
public interface UserMapper {

    @Mapping(source = "userType", target = "type")
    UserDto map(User source);
}
