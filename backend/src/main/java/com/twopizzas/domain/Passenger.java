package com.twopizzas.domain;

import com.twopizzas.data.Entity;
import com.twopizzas.util.AssertionConcern;

import java.time.LocalDate;
import java.util.Objects;

public class Passenger extends AssertionConcern implements Entity<EntityId> {
    private final EntityId id;
    private final String givenName;
    private final String surname;
    private final LocalDate dateOfBirth;
    private final String nationality;
    private final String passportNumber;
    private Booking booking;

    public Passenger(EntityId id, String givenName, String surname, LocalDate dateOfBirth, String nationality, String passportNumber, Booking booking) {
        this.id = notNull(id, "id");
        this.givenName = notNullAndNotBlank(givenName, "givenName");
        this.surname = notNullAndNotBlank(surname, "surname");
        this.dateOfBirth = notNull(dateOfBirth, "dateOfBirth");
        this.nationality = notNullAndNotBlank(nationality, "nationality");
        this.passportNumber = notNullAndNotBlank(passportNumber, "passportNumber");
        this.booking = notNull(booking, "booking");
    }

    public Passenger(String givenName, String surname, LocalDate dateOfBirth, String nationality, String passportNumber) {
        this(EntityId.nextId(), givenName, surname, dateOfBirth, nationality, passportNumber, null);
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getSurname() {
        return surname;
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

    public Booking getBooking() { return booking; }
}
