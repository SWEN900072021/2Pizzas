package com.twopizzas.api.airplaneProfile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewAirplaneProfileDto {
    private String code;
    private String type;
    private int firstClassRows;
    private int firstClassColumns;
    private int businessClassRows;
    private int businessClassColumns;
    private int economyClassRows;
    private int economyClassColumns;

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (code == null || code.trim().isEmpty()) {
            errors.add("code must not be blank");
        }
        if (type == null || type.trim().isEmpty()) {
            errors.add("type must not be blank");
        }
        if (firstClassRows < 0) {
            errors.add("firstClassRows must not be less than 0");
        }
        if (firstClassColumns < 0) {
            errors.add("firstClassColumns must not be less than 0");
        }
        if (businessClassRows < 0) {
            errors.add("businessClassRows must not be less than 0");
        }
        if (businessClassColumns < 0) {
            errors.add("businessClassColumns must not be less than 0");
        }
        if (economyClassRows < 0) {
            errors.add("economyClassRows must not be less than 0");
        }
        if (economyClassColumns < 0) {
            errors.add("economyClassColumns must not be less than 0");
        }
        return errors;
    }
}
