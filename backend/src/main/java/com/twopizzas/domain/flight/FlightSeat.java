package com.twopizzas.domain.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.Passenger;
import com.twopizzas.util.AssertionConcern;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightSeat extends AssertionConcern {

    private static final List<Character> VALID_COLUMNS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().mapToObj(e -> (char) e).collect(Collectors.toList());
    private final String name;
    private ValueHolder<Passenger> passengerValueHolder;
    private final SeatClass seatClass;

    public FlightSeat(String name, ValueHolder<Passenger> passengerValueHolder, SeatClass seatClass) {
        this.name = notNullAndNotBlank(name, "name");
        this.passengerValueHolder = notNull(passengerValueHolder, "passenger");
        this.seatClass = notNull(seatClass, "seatClass");
    }

    public FlightSeat(String name, SeatClass seatClass) {
        this(name, () -> null, seatClass);
    }

    public String getName() {
        return name;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public boolean isBooked() {
        return getPassenger().isPresent();
    }

    public Optional<Passenger> getPassenger() {
        Passenger maybePassenger = passengerValueHolder.get();
        if (maybePassenger != null) {
            return Optional.of(maybePassenger);
        }
        return Optional.empty();
    }

    public void setPassenger(Passenger passenger) {
        this.passengerValueHolder = () -> passenger;
    }
}
