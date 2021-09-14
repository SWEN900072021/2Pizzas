package com.twopizzas.api;

import lombok.Data;

@Data
public class SignupRequestDTO {

    private String username;

    private String password;

    private String email;

    private String givenName;

    private String surname;
}
