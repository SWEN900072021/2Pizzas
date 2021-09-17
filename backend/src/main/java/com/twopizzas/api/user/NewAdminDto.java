package com.twopizzas.api.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewAdminDto {
    private String username;
    private String password;

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (username == null || username.trim().isEmpty()) {
            errors.add("userName must not be blank");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("password must not be blank");
        }

        return errors;
    }
}
