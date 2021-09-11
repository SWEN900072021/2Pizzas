package com.twopizzas.domain;

import com.twopizzas.data.Repository;

import java.util.List;

public interface BookingRepository extends Repository<Booking, EntityId> {
    List<Booking> findAllFlightBookings(EntityId flightId);
}
