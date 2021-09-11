package com.twopizzas.port.data.booking;

import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class FlightBookingsSpecification implements BookingSpecification {

    private static final String TEMPLATE =
            "SELECT * FROM booking WHERE flightId = ?;";

    private final EntityId flightId;
    private final BookingMapper bookingMapper;

    public FlightBookingsSpecification(EntityId flightId, BookingMapper bookingMapper) {
        this.flightId = flightId;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public List<Booking> execute(ConnectionPool context) {
        return new SqlStatement(TEMPLATE, flightId.toString()).doQuery(context.getCurrentTransaction(), bookingMapper);
    }
}
