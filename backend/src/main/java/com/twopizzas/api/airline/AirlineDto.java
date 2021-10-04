package com.twopizzas.api.airline;

import com.twopizzas.domain.user.User;
import lombok.Data;

@Data
public class AirlineDto {
    private String id;
    private String name;
    private String code;
    private User.UserStatus status;
}
