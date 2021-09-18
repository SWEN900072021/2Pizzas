package com.twopizzas.api.airplaneProfile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewAirplaneProfileDto {
    private String code;
    private String type;
    private Integer firstClassRows;
    private Integer firstClassColumns;
    private Integer businessClassRows;
    private Integer businessClassColumns;
    private Integer economyClassRows;
    private Integer economyClassColumns;

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (code == null || code.trim().isEmpty()) {
            errors.add("code must not be blank");
        }
        if (type == null || type.trim().isEmpty()) {
            errors.add("type must not be blank");
        }
        if (firstClassRows == null) {
            errors.add("first class rows must not be blank");
        }
        if (firstClassColumns == null) {
            errors.add("first class columns must not be blank");
        }
        if (businessClassRows == null) {
            errors.add("business class rows must not be blank");
        }
        if (businessClassColumns == null) {
            errors.add("business class columns must not be blank");
        }
        if (economyClassRows == null) {
            errors.add("economy class rows must not be blank");
        }
        if (economyClassColumns == null) {
            errors.add("economy class columns must not be blank");
        }
        return errors;
    }
}
