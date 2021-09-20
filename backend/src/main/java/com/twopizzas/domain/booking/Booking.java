package com.twopizzas.domain.booking;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
public class Booking extends DomainEntity {

    private final OffsetDateTime date;
    private BigDecimal totalCost;
    private final Customer customer;

    private SeatBooking flightBooking;
    private SeatBooking returnFlightBooking;

    public Booking(EntityId id, OffsetDateTime date, BigDecimal totalCost, Customer customer) {
        super(id);
        this.date = notNull(date, "date");
        this.totalCost = totalCost;
        this.customer = notNull(customer, "customer");
    }

    public Booking(Customer customer) {
        this(EntityId.nextId(), OffsetDateTime.now(), null, customer);
    }


    public void addFlight(SeatBooking seatBooking) {
        flightBooking = seatBooking;
    }

    public void addReturnFlight(SeatBooking seatBooking) {
        returnFlightBooking = seatBooking;
    }

    public SeatBooking getFlightBooking() {
        return flightBooking;
    }

    public SeatBooking getReturnFlightBooking() {
        return returnFlightBooking;
    }
}
