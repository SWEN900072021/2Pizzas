package com.twopizzas.api.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponseDto {
    private String token;
    private String username;
    private String userType;
}
