package com.twopizzas.domain.flight;

import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.*;
import com.twopizzas.util.AssertionConcern;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Flight extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final AirplaneProfile airplaneProfile;
    private final Airline airline;
    private final ValueHolder<List<FlightSeat>> seats;
    private final ValueHolder<List<FlightSeatAllocation>> seatAllocations;
    private final Airport origin;
    private final Airport destination;
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    private final List<StopOver> stopOvers;
    private final String code;
    private Status status;

    public Flight(EntityId id, ValueHolder<List<Booking>> bookings, AirplaneProfile airplaneProfile, Airline airline, ValueHolder<List<FlightSeat>> seats, Airport origin, Airport destination, List<StopOver> stopOvers, String code) {
        this.id = notNull(id, "id");
        this.bookings = notNull(bookings, "bookings");
        this.airplaneProfile = notNull(airplaneProfile, "airplaneProfile");
        this.airline = notNull(airline, "airline");
        this.seats = notNull(seats, "seats");
        this.origin = notNull(origin, "origin");
        this.destination = notNull(destination, "destination");
        this.stopOvers = notNull(stopOvers, "stopOvers");
        this.code = notNull(code, "code");
    }

    public Flight(AirplaneProfile airplaneProfile, Airline airline, Airport origin, Airport destination, List<StopOver> stopOvers, String code) {
        this(EntityId.nextId(), ArrayList::new, airplaneProfile, airline, () -> buildSeats(airplaneProfile), origin, destination, stopOvers, code);
    }

    private static List<FlightSeat> buildSeats(AirplaneProfile profile) {
        return profile.getSeatProfiles().stream().map(
                        sp -> new FlightSeat(sp.getName(), sp.getSeatClass())
                ).collect(Collectors.toList());
    }

    public SeatAllocation allocateSeats(BookingRequest request) {
        List<String> bookedSeatNames = seatAllocations.get().stream()
                .filter(FlightSeat::isBooked)
                .map(FlightSeat::getName)
                .collect(Collectors.toList());

        return null;
    }

    public List<Passenger> getPassengers() {
        return seats.get().stream()
                .map(FlightSeat::getPassenger)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<FlightSeat> getSeats() {
        return seats.get();
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public enum Status {
        CANCELLED,
        TO_SCHEDULE,
        DELAYED,
    }
}
