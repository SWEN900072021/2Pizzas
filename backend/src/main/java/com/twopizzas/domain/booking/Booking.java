package com.twopizzas.domain.booking;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.port.data.DomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
public class Booking extends DomainEntity {

    private final OffsetDateTime date;
    private final Customer customer;

    private SeatBooking flightBooking;
    private SeatBooking returnFlightBooking;

    public Booking(EntityId id, OffsetDateTime date, Customer customer) {
        super(id);
        this.date = notNull(date, "date");
        this.customer = notNull(customer, "customer");
    }

    public Booking(Customer customer) {
        this(EntityId.nextId(), OffsetDateTime.now(), customer);
    }


    public void addFlight(SeatBooking seatBooking) {
        flightBooking = seatBooking;
    }

    public void addReturnFlight(SeatBooking seatBooking) {
        returnFlightBooking = seatBooking;
    }

    public BigDecimal getTotalCost() {
        BigDecimal totalCost = BigDecimal.valueOf(0);
        if (flightBooking != null) {
            totalCost = totalCost.add(getTotalCostForBooking(flightBooking));
        }

        if (returnFlightBooking != null) {
            totalCost = totalCost.add(getTotalCostForBooking(returnFlightBooking));
        }
        return totalCost;
    }

    private BigDecimal getTotalCostForBooking(SeatBooking seatBooking) {
        BigDecimal totalCost = BigDecimal.valueOf(0);
        for (FlightSeatAllocation allocation : seatBooking.getAllocations()) {
            totalCost = totalCost.add(allocation.getCost());
        }
        return totalCost;
    }

    public SeatBooking getFlightBooking() {
        return flightBooking;
    }

    public SeatBooking getReturnFlightBooking() {
        return returnFlightBooking;
    }
}
