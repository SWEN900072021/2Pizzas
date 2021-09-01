package com.twopizzas.domain;

import com.twopizzas.data.ValueHolder;
import com.twopizzas.util.AssertionConcern;

import java.util.Date;

public class Passenger extends AssertionConcern {
    private final String firstName;
    private final String lastName;
    private final Date dateOfBirth;
    private final String passportNumber;
    private final ValueHolder<Seat> seat;

    public Passenger(String firstName, String lastName, Date dateOfBirth, String passportNumber, ValueHolder<Seat> seat) {
        this.firstName = notBlank(notNull(firstName, "firstName"), "firstName");
        this.lastName = notBlank(notNull(lastName, "lastName"), "lastName");
        this.dateOfBirth = notNull(dateOfBirth, "dateOfBirth");
        this.passportNumber = notBlank(notNull(passportNumber, "passportNumber"), "passportNumber");
        this.seat = notNull(seat, "seat");
    }

    public Passenger(String firstName, String lastName, Date dateOfBirth, String passportNumber, Seat seat) {
        this(firstName, lastName, dateOfBirth, passportNumber, () -> seat);
        notNull(seat, "seat");
    }


}
