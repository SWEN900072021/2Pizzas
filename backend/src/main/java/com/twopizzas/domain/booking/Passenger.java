package com.twopizzas.domain.booking;

import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Passenger extends DomainEntity {
    private final String givenName;
    private final String surname;
    private final LocalDate dateOfBirth;
    private final String nationality;
    private final String passportNumber;
    private ValueHolder<Booking> booking;

    public Passenger(EntityId id, String givenName, String surname, LocalDate dateOfBirth, String nationality, String passportNumber, ValueHolder<Booking> booking) {
        super(id);
        this.givenName = notNullAndNotBlank(givenName, "givenName");
        this.surname = notNullAndNotBlank(surname, "surname");
        this.dateOfBirth = notNull(dateOfBirth, "dateOfBirth");
        this.nationality = notNullAndNotBlank(nationality, "nationality");
        this.passportNumber = notNullAndNotBlank(passportNumber, "passportNumber");
        this.booking = booking;
    }

    public Passenger(String givenName, String surname, LocalDate dateOfBirth, String nationality, String passportNumber) {
        this(EntityId.nextId(), givenName, surname, dateOfBirth, nationality, passportNumber, () -> null);
    }

    public Booking getBooking() {
        return booking.get();
    }

    public void setBooking(Booking booking) {
        this.booking = () -> booking;
    }
}
