package com.twopizzas.api.user;

import com.twopizzas.domain.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserUpdateDto {
    private User.Status status;

    List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (status == null) {
            errors.add("status is required");
        }
        return errors;
    }
}
