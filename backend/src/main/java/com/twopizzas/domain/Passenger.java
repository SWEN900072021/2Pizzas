package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

import java.util.Date;

public class Passenger extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;
    private final String givenName;
    private final String lastName;
    private final Date dateOfBirth;
    private final String passportNumber;

    public Passenger(EntityId id, String givenName, String lastName, Date dateOfBirth, String passportNumber) {
        this.id = notNull(id, "id");
        this.givenName = notNullAndNotBlank(givenName, "givenName");
        this.lastName = notNullAndNotBlank(lastName, "lastName");
        this.dateOfBirth = notNull(dateOfBirth, "dateOfBirth");
        this.passportNumber = notNullAndNotBlank(passportNumber, "passportNumber");
    }

    public Passenger(String givenName, String lastName, Date dateOfBirth, String passportNumber) {
        this(EntityId.nextId(), givenName, lastName, dateOfBirth, passportNumber);
    }

    @Override
    public EntityId getId() {
        return id;
    }
}
