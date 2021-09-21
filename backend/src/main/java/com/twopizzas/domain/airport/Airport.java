package com.twopizzas.domain.airport;

import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;

import java.time.ZoneId;
import java.util.Objects;

@Getter
public class Airport extends DomainEntity {

    private static final int MAX_AIRPORT_CODE_LENGTH = 3;

    private final String code;
    private final String name;
    private final String location;
    private final ZoneId utcOffset;

    public Airport(EntityId id, String code, String name, String location, ZoneId utcOffset) {
        super(id);
        this.code = max(notNullAndNotBlank(code, "code"), MAX_AIRPORT_CODE_LENGTH, "code");
        this.name = notNullAndNotBlank(name, "name");
        this.location = notNullAndNotBlank(location, "location");
        this.utcOffset = notNull(utcOffset, "utcOffset");
    }

    public Airport(String code, String name, String location, ZoneId utcOffset) {
        this(EntityId.nextId(), code, name, location, utcOffset);
    }
}
