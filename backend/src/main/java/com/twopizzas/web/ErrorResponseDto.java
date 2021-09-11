package com.twopizzas.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
public class ErrorResponseDto {

    @EqualsAndHashCode.Include
    private int status;

    private String message;

}
