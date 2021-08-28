package com.twopizzas.data;

public class DataConsistencyViolation extends RuntimeException {
    DataConsistencyViolation(String message) {
        super(message);
    }
}
