package com.twopizzas.api;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}
