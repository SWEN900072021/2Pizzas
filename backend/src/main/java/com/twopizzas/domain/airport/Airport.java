package com.twopizzas.domain.airport;

import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;

@Getter
public class Airport extends DomainEntity {

    private static final int MAX_AIRPORT_CODE_LENGTH = 3;

    @Setter
    private AirportStatus status;

    private final String code;
    private final String name;
    private final String location;
    private final ZoneId utcOffset;

    public Airport(EntityId id, String code, String name, String location, ZoneId utcOffset, AirportStatus status) {
        super(id);
        this.code = max(notNullAndNotBlank(code, "code"), MAX_AIRPORT_CODE_LENGTH, "code");
        this.name = notNullAndNotBlank(name, "name");
        this.location = notNullAndNotBlank(location, "location");
        this.utcOffset = notNull(utcOffset, "utcOffset");
        this.status = status;
    }

    public Airport(String code, String name, String location, ZoneId utcOffset) {
        this(EntityId.nextId(), code, name, location, utcOffset, AirportStatus.ACTIVE);
    }

    public enum AirportStatus {
        ACTIVE,
        INACTIVE
    }
}
