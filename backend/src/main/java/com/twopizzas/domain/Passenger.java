package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

import java.time.LocalDate;

public class Passenger extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;
    private final String givenName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String nationality;
    private final String passportNumber;

    public Passenger(EntityId id, String givenName, String lastName, LocalDate dateOfBirth, String nationality, String passportNumber) {
        this.id = notNull(id, "id");
        this.givenName = notNullAndNotBlank(givenName, "givenName");
        this.lastName = notNullAndNotBlank(lastName, "lastName");
        this.dateOfBirth = notNull(dateOfBirth, "dateOfBirth");
        this.nationality = notNullAndNotBlank(nationality, "nationality");
        this.passportNumber = notNullAndNotBlank(passportNumber, "passportNumber");
    }

    @Override
    public EntityId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return Entity.super.isNew();
    }

    public String getGivenName() {
        return givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
}
