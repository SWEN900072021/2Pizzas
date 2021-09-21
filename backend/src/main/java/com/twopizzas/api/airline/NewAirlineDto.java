package com.twopizzas.api.airline;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewAirlineDto {

    private String name;
    private String username;
    private String password;
    private String code;

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            errors.add("name must not be blank");
        }

        if (username == null || username.trim().isEmpty()) {
            errors.add("userName must not be blank");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("password must not be blank");
        }

        if (code == null || code.trim().isEmpty()) {
            errors.add("code must not be blank");
        }

        return errors;
    }
}
