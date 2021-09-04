package com.twopizzas.domain.flight;

import com.twopizzas.data.Entity;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.*;
import com.twopizzas.domain.error.BusinessRuleException;
import com.twopizzas.domain.error.DataFormatException;
import com.twopizzas.util.AssertionConcern;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Flight extends AssertionConcern implements Entity<EntityId> {

    private final EntityId id;
    private final AirplaneProfile airplaneProfile;
    private final Airline airline;
    private final ValueHolder<Set<FlightSeat>> seats;
    private final ValueHolder<Set<FlightSeatAllocation>> allocatedSeats;
    private final Airport origin;
    private final Airport destination;
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    private final List<StopOver> stopOvers;
    private final String code;
    private Status status;

    public Flight(EntityId id, ValueHolder<Set<FlightSeatAllocation>> allocatedSeats, AirplaneProfile airplaneProfile, Airline airline, ValueHolder<Set<FlightSeat>> seats, Airport origin, Airport destination, List<StopOver> stopOvers, String code) {
        this.id = notNull(id, "id");
        this.allocatedSeats = notNull(allocatedSeats, "bookedSeats");
        this.airplaneProfile = notNull(airplaneProfile, "airplaneProfile");
        this.airline = notNull(airline, "airline");

        if(seats == null) {
            this.seats = () -> buildSeats(airplaneProfile);
        } else {
            this.seats = seats;
        }

        this.origin = notNull(origin, "origin");
        this.destination = notNull(destination, "destination");
        this.stopOvers = notNull(stopOvers, "stopOvers");
        this.code = notNull(code, "code");
    }

    public Flight(AirplaneProfile airplaneProfile, Airline airline, Airport origin, Airport destination, List<StopOver> stopOvers, String code) {
        this(EntityId.nextId(), HashSet::new, airplaneProfile, airline, null, origin, destination, stopOvers, code);
    }

    private Set<FlightSeat> buildSeats(AirplaneProfile profile) {
        return profile.getSeatProfiles().stream().map(
                        sp -> new FlightSeat(sp.getName(), sp.getSeatClass(), this)
                ).collect(Collectors.toSet());
    }

    public SeatBooking allocateSeats(BookingRequest request) {
        // assert that the request is valid
        Set<String> seatNames = request.getAllocations().stream().map(BookingRequest.SeatAllocationRequest::getSeatName).collect(Collectors.toSet());
        if (seatNames.size() != request.getAllocations().size()) {
            throw new DataFormatException("all seat names across requested allocations must be unique");
        }

        if (request.getAllocations().stream()
                .map(BookingRequest.SeatAllocationRequest::getPassenger)
                .count() != request.getAllocations().size()) {
            throw new DataFormatException("all passengers across requested allocations must be unique");
        }

        Map<String, Passenger> passengerSeatNames = request.getAllocations().stream()
                .collect(Collectors.toMap(
                        BookingRequest.SeatAllocationRequest::getSeatName,
                        BookingRequest.SeatAllocationRequest::getPassenger
                ));

        Set<FlightSeat> seatsToBook = getSeats(seatNames);
        Set<FlightSeat> availableSeats = getAvailableSeats();

        Set<FlightSeat> bookingConflicts = getSeats().stream()
                .filter(s -> !availableSeats.contains(s))
                .collect(Collectors.toSet());

        if (!bookingConflicts.isEmpty()) {
            throw new BusinessRuleException(String.format("seats %s are already booked for flight %s",
                    id,
                    bookingConflicts.stream().map(FlightSeat::getName).collect(Collectors.toList())));
        }

        Set<FlightSeatAllocation> newAllocations = availableSeats.stream()
                .map(s -> new FlightSeatAllocation(s, passengerSeatNames.get(s.getName())))
                .collect(Collectors.toSet());

        allocatedSeats.get().addAll(newAllocations);

        return new SeatBooking(
                this,
                newAllocations
        );
    }

    private Set<FlightSeat> getSeats(Set<String> seatNames) {
        // assert that the seat names are correct for the fight
        Set<FlightSeat> matchingSeats = seats.get().stream()
                .filter(s -> seatNames.contains(s.getName()))
                .collect(Collectors.toSet());

        if (matchingSeats.size() != seatNames.size()) {
            List<String> matchedNames = matchingSeats.stream().map(FlightSeat::getName).collect(Collectors.toList());
            throw new BusinessRuleException(String.format("seats %s are invalid for flight %s",
                    id,
                    seatNames.stream().filter(n -> !matchedNames.contains(n)).collect(Collectors.toList())));
        }

        return matchingSeats;
    }

    public Set<FlightSeat> getAvailableSeats() {
        Set<FlightSeat> bookedSeats = allocatedSeats.get().stream()
                .map(FlightSeatAllocation::getSeat)
                .collect(Collectors.toSet());

        return seats.get().stream().filter(s -> !bookedSeats.contains(s)).collect(Collectors.toSet());
    }

    public Set<Passenger> getPassengers() {
        return allocatedSeats.get().stream()
                .map(FlightSeatAllocation::getPassenger)
                .collect(Collectors.toSet());
    }

    public Set<FlightSeat> getSeats() {
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
