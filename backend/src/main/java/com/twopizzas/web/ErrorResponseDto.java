package com.twopizzas.web;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponseDto {

    private String url;
    private int status;
    private String message;
    private String reason;

}
