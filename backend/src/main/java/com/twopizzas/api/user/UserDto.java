package com.twopizzas.api.user;

import com.twopizzas.domain.user.User;
import lombok.Data;

@Data
public class UserDto {
    private String type;
    private String username;
    private String id;
    private User.Status status;
}
