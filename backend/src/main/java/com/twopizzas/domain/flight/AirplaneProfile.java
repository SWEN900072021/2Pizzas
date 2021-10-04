package com.twopizzas.domain.flight;

import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AirplaneProfile extends DomainEntity {
    private final String code;
    private final String type;
    private final int firstClassRows;
    private final int firstClassColumns;
    private final int businessClassRows;
    private final int businessClassColumns;
    private final int economyClassRows;
    private final int economyClassColumns;

    public AirplaneProfile(EntityId id, String code, String type, int firstClassRows, int firstClassColumns,
                           int businessClassRows, int businessClassColumns, int economyClassRows, int economyClassColumns, long version) {
        super(id, version);
        this.code = notNullAndNotBlank(code, "code");
        this.type = notNullAndNotBlank(type, "type");
        this.firstClassRows = notNull(firstClassRows, "firstClassRows");
        this.firstClassColumns = notNull(firstClassColumns, "firstClassColumns");
        this.businessClassRows = notNull(businessClassRows, "businessClassRows");
        this.businessClassColumns = notNull(businessClassColumns, "businessClassColumns");
        this.economyClassRows = notNull(economyClassRows, "economyClassRows");
        this.economyClassColumns = notNull(economyClassColumns, "economyClassColumns");
    }

    public AirplaneProfile(String code, String type, int firstClassRows, int firstClassColumns,
                           int businessClassRows, int businessClassColumns, int economyClassRows, int economyClassColumns) {
        this(EntityId.nextId(), code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns, 0);
    }

    public List<SeatProfile> getSeatProfiles() {
        List<SeatProfile> allSeats = new ArrayList<>();
        allSeats.addAll(getSeatProfilesForClass(0, firstClassRows, firstClassColumns, SeatClass.FIRST));
        allSeats.addAll(getSeatProfilesForClass(firstClassRows, businessClassRows, businessClassColumns, SeatClass.BUSINESS));
        allSeats.addAll(getSeatProfilesForClass(businessClassRows + firstClassRows, economyClassRows, economyClassColumns, SeatClass.ECONOMY));
        return  allSeats;
    }

    private List<SeatProfile> getSeatProfilesForClass(int rowOffset, int rows, int columns, SeatClass seatClass) {
        List<SeatProfile> seatProfiles = new ArrayList<>();
        for (int rowId = 0; rowId < rows; rowId++) {
            for (int columnId = 0; columnId < columns; columnId++) {
                seatProfiles.add(new SeatProfile(rowId + rowOffset, columnId, seatClass));
            }
        }
        return seatProfiles;
    }

    public List<FlightSeat> getFlightSeats(Flight flight) {
        return getSeatProfiles().stream().map(s -> new FlightSeat(s.getName(), s.getSeatClass(), flight)).collect(Collectors.toList());
    }
}
