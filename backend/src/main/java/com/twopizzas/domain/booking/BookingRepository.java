package com.twopizzas.domain.booking;

import com.twopizzas.data.Repository;
import com.twopizzas.domain.EntityId;

import java.util.List;

public interface BookingRepository extends Repository<Booking, EntityId> {
    List<Booking> findAllFlightBookings(EntityId flightId);
    List<Booking> findAllCustomerBookings(EntityId customerId);
}
