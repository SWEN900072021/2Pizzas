package com.twopizzas.domain.flight;

import com.twopizzas.data.Entity;
import com.twopizzas.domain.Airline;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.util.AssertionConcern;

import java.util.ArrayList;
import java.util.List;

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

    public Airplane(EntityId id, String code, String type, int firstClassRows, int firstClassColumns,
                           int businessClassRows, int businessClassColumns, int economyClassRows, int economyClassColumns) {
        this.id = notNull(id, "id");
        this.code = notNullAndNotBlank(code, "code");
        this.type = notNullAndNotBlank(type, "type");
        this.firstClassRows = notNull(firstClassRows, "firstClassRows");
        this.firstClassColums = notNull(firstClassColumns, "firstClassColumns");
        this.businessClassRows = notNull(businessClassRows, "businessClassRows");
        this.businessClassColums = notNull(businessClassColumns, "businessClassColumns");
        this.economyClassRows = notNull(economyClassRows, "economyClassRows");
        this.economyClassColums = notNull(economyClassColumns, "economyClassColumns");
    }

    public List<SeatProfile> getSeatProfiles() {
        List<SeatProfile> allSeats = new ArrayList<>();
        allSeats.addAll(getSeatProfilesForClass(firstClassRows, firstClassColumns, SeatClass.FIRST));
        allSeats.addAll(getSeatProfilesForClass(businessClassRows, businessClassColumns, SeatClass.BUSINESS));
        allSeats.addAll(getSeatProfilesForClass(economyClassRows, economyClassColumns, SeatClass.ECONOMY));
        return  allSeats;
    }

    private List<SeatProfile> getSeatProfilesForClass(int rows, int columns, SeatClass seatClass) {
        List<SeatProfile> seatProfiles = new ArrayList<>();
        for (int rowId = 0; rowId < rows; rowId++) {
            for (int columnId = 0; columnId < columns; columnId++) {
                seatProfiles.add(new SeatProfile(rowId, columnId, seatClass));
            }
        }
        return seatProfiles;
    }

    @Override
    public EntityId getId() { return id; }

    public String getCode() { return code; }

    public String getType() { return type; }
}
