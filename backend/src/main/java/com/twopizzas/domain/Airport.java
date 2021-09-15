package com.twopizzas.domain;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(id, airport.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
