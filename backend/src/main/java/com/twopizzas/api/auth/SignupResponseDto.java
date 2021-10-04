package com.twopizzas.api.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignupResponseDto {
    private String id;
    private String userName;
    private String givenName;
    private String surname;
    private String email;
    private String token;
}
