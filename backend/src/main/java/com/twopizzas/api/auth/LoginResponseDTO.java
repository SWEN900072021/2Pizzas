package com.twopizzas.api.auth;

import com.twopizzas.domain.user.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponseDTO {
    private String token;
    private User user;
}
