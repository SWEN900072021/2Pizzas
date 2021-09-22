package com.twopizzas.api.auth;

import lombok.Data;

@Data
public class SignupRequestDTO {
    private String username;
    private String password;
    private String email;
    private String givenName;
    private String surname;
    private String userType;
}
