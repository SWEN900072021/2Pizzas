package com.twopizzas.domain;

import com.twopizzas.data.ValueHolder;
import com.twopizzas.util.AssertionConcern;

import java.util.Date;

public class Passenger extends AssertionConcern {
    private final String givenName;
    private final String lastName;
    private final Date dateOfBirth;
    private final String passportNumber;
    private final ValueHolder<Seat> seat;

    public Passenger(String givenName, String lastName, Date dateOfBirth, String passportNumber, ValueHolder<Seat> seat) {
        this.givenName = notBlank(notNull(givenName, "givenName"), "givenName");
        this.lastName = notBlank(notNull(lastName, "lastName"), "lastName");
        this.dateOfBirth = notNull(dateOfBirth, "dateOfBirth");
        this.passportNumber = notBlank(notNull(passportNumber, "passportNumber"), "passportNumber");
        this.seat = notNull(seat, "seat");
    }

    public Passenger(String givenName, String lastName, Date dateOfBirth, String passportNumber, Seat seat) {
        this(givenName, lastName, dateOfBirth, passportNumber, () -> seat);
        notNull(seat, "seat");
    }


}
