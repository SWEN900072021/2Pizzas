package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

public class AirplaneProfile extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;
    private String code;
    private String type;
    private int firstClassRows;
    private int firstClassColumns;
    private int businessClassRows;
    private int businessClassColumns;
    private int economyClassRows;
    private int economyClassColumns;

    AirplaneProfile(EntityId id) {
        notNull(id, "id");
        this.id = id;
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public String getCode() { return code; }

    public String getType() { return type; }

    public int getFirstClassRows() { return firstClassRows; }

    public int getFirstClassColumns() { return firstClassColumns; }

    public int getBusinessClassRows() { return businessClassRows; }

    public int getBusinessClassColumns() { return businessClassColumns; }

    public int getEconomyClassRows() { return economyClassRows; }

    public int getEconomyClassColumns() { return economyClassColumns; }

    public int getCapacity() {
        int firstClassCapacity = firstClassRows * firstClassColumns;
        int businessCapacity = businessClassRows * businessClassColumns;
        int economyCapacity = economyClassRows * economyClassColumns;

        return firstClassCapacity + businessCapacity + economyCapacity;
    }
}
