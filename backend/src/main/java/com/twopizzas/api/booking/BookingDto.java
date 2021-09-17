package com.twopizzas.api.booking;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookingDto {
    private String id;
}
