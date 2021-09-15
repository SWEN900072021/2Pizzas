package com.twopizzas.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookingResponseDto {
    private String id;
}
