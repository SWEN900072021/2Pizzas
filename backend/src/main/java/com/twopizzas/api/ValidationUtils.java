package com.twopizzas.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {
    public static boolean isUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOffsetDateTime(String value) {
        try {
            OffsetDateTime.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
