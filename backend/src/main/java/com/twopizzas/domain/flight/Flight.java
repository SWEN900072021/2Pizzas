package com.twopizzas.domain.flight;

import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.*;
import com.twopizzas.error.BuisnessRuleException;
import com.twopizzas.error.BusinessRuleException;
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
    private final ValueHolder<List<FlightSeatAllocation>> bookedSeats;
    private final Airport origin;
    private final Airport destination;
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    private final List<StopOver> stopOvers;
    private final String code;
    private Status status;

    public Flight(EntityId id, ValueHolder<List<FlightSeatAllocation>> bookedSeats, AirplaneProfile airplaneProfile, Airline airline, ValueHolder<List<FlightSeat>> seats, Airport origin, Airport destination, List<StopOver> stopOvers, String code) {
        this.id = notNull(id, "id");
        this.bookedSeats = notNull(bookedSeats, "bookedSeats");
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

    public SeatBooking allocateSeats(BookingRequest request) {
        // assert that the flight has the correct seats
        List<String> allSeatNames = seats.get().stream()
                .map(FlightSeat::getName)
                .collect(Collectors.toList());

        List<String> invalidSeatNames = request.getAllocations().stream()
                .map(BookingRequest.SeatAllocationRequest::getSeatName)
                .filter(allSeatNames::contains)
                .collect(Collectors.toList());

        if (!invalidSeatNames.isEmpty()) {
            throw new BusinessRuleException(String.format("seats %s are invalid for flight %s", id, invalidSeatNames));
        }

        List<String> bookedSeatNames = bookedSeats.get().stream()
                .map(a -> a.getSeat().getName())
                .collect(Collectors.toList());

        // assert that the seats are not booked
        List<String> invalidSeatBookings = request.getAllocations().stream()
                .map(BookingRequest.SeatAllocationRequest::getSeatName)
                .filter(bookedSeatNames::contains)
                .collect(Collectors.toList());

        if (!invalidSeatBookings.isEmpty()) {
            throw new BusinessRuleException(String.format("seats %s are already booked for flight %s", id, invalidSeatBookings));
        }

        return new SeatBooking(
                this, seats.get().stream().filter()
        )
    }

    public List<FlightSeat> getAvailableSeats(List<String> seatNames) {
        // assert that the flight has the correct seats
        List<FlightSeat> matchingSeats = seats.get().stream()
                .filter(s -> seatNames.contains(s.getName()))
                .collect(Collectors.toList());

        if (matchingSeats.size() != seatNames.size()) {
            throw new BusinessRuleException(String.format("seats %s are invalid for flight %s", id, invalidSeatNames));
        }

        List<String> bookedSeatNames = bookedSeats.get().stream()
                .map(a -> a.getSeat().getName())
                .collect(Collectors.toList());

        // assert that the seats are not booked
        List<String> invalidSeatBookings = seatNames.stream()
                .filter(bookedSeatNames::contains)
                .collect(Collectors.toList());

        if (!invalidSeatBookings.isEmpty()) {
            throw new BusinessRuleException(String.format("seats %s are already booked for flight %s", id, invalidSeatBookings));
        }


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
