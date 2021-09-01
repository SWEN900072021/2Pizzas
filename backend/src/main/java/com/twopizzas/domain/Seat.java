package com.twopizzas.domain;

import com.twopizzas.data.ValueHolder;
import com.twopizzas.util.AssertionConcern;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Seat extends AssertionConcern {

    private static final List<Character> VALID_COLUMNS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().mapToObj(e -> (char) e).collect(Collectors.toList());
    private final int rowId;
    private final char columnLetter;
    private final ValueHolder<Flight> flight;
    private final ValueHolder<Booking> booking;
    private final SeatClass seatClass;

    public Seat(int rowId, char columnLetter, ValueHolder<Flight> flight, ValueHolder<Booking> booking, SeatClass seatClass) {
        this.rowId = min(rowId, 1, "rowId");
        this.columnLetter = oneOf(Character.toUpperCase(columnLetter), VALID_COLUMNS, "columnLetter");
        this.flight = notNull(flight, "flight");
        this.booking = notNull(booking, "booking");
        this.seatClass = notNull(seatClass, "seatClass");
    }

    public Seat(int rowId, char columnLetter, Flight flight, SeatClass seatClass) {
        this(rowId, columnLetter, () -> flight, () -> null, seatClass);
        notNull(flight, "flight");
    }

    public String getName() {
        return String.format("%s%s", rowId, columnLetter);
    }

    public enum SeatClass {
        FIRST, BUSINESS, ECONOMY,
    }

    public Flight getFlight() {
        return flight.get();
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public Optional<Booking> getBooking() {
        Booking maybeBooking = booking.get();
        if (maybeBooking != null) {
            return Optional.of(maybeBooking);
        }
        return Optional.empty();
    }
}
