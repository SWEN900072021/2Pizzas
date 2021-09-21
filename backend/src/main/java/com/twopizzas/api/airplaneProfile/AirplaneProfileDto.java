package com.twopizzas.api.airplaneProfile;

import lombok.Data;

@Data
public class AirplaneProfileDto {
    private String id;
    private String code;
    private String type;
    private int firstClassRows;
    private int firstClassColumns;
    private int businessClassRows;
    private int businessClassColumns;
    private int economyClassRows;
    private int economyClassColumns;
}
