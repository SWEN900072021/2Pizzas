package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

import java.time.LocalDate;
import java.util.Objects;

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

    public Passenger(String givenName, String lastName, LocalDate dateOfBirth, String nationality, String passportNumber) {
        this(EntityId.nextId(), givenName, lastName, dateOfBirth, nationality, passportNumber);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id.equals(passenger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
