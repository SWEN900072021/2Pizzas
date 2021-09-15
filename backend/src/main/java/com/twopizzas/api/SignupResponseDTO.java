package com.twopizzas.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignupResponseDTO {
    private String id;
    private String userName;
    private String givenName;
    private String surname;
    private String email;
}
