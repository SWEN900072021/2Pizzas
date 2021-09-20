package com.twopizzas.port.data.booking;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class CustomerBookingsSpecification implements BookingSpecification {
    private static final String TEMPLATE =
            "SELECT * FROM booking WHERE customerId = ?;";

    private final EntityId customerId;
    private final BookingMapper bookingMapper;

    public CustomerBookingsSpecification(EntityId customerId, BookingMapper bookingMapper) {
        this.customerId = customerId;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public List<Booking> execute(ConnectionPool context) {
        return new SqlStatement(TEMPLATE, customerId.toString()).doQuery(context.getCurrentTransaction(), bookingMapper);
    }
}
