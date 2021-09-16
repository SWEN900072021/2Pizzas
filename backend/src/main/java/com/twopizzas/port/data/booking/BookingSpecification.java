package com.twopizzas.port.data.booking;

import com.twopizzas.data.Specification;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.port.data.db.ConnectionPool;

public interface BookingSpecification extends Specification<Booking, ConnectionPool> { }
