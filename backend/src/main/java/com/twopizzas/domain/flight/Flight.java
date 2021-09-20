package com.twopizzas.domain.flight;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.*;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.domain.error.BusinessRuleException;
import com.twopizzas.domain.error.DataFormatException;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.port.data.DomainEntity;
import com.twopizzas.util.ValueViolation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Flight extends DomainEntity {

    private final AirplaneProfile airplaneProfile;
    private final Airline airline;
    private final ValueHolder<List<FlightSeat>> seats;
    private final ValueHolder<List<FlightSeatAllocation>> allocatedSeats;
    private final Airport origin;
    private final Airport destination;
    private final OffsetDateTime departure;
    private final OffsetDateTime arrival;
    private final List<StopOver> stopOvers;
    private final String code;
    private final BigDecimal firstClassCost;
    private final BigDecimal businessClassCost;
    private final BigDecimal economyClassCost;

    @Setter
    private Status status;

    public Flight(EntityId id, ValueHolder<List<FlightSeatAllocation>> allocatedSeats, AirplaneProfile airplaneProfile,
                  Airline airline, ValueHolder<List<FlightSeat>> seats, Airport origin, Airport destination,
                  OffsetDateTime departure, OffsetDateTime arrival, List<StopOver> stopOvers, String code, Status status,
                  BigDecimal firstClassCost, BigDecimal businessClassCost, BigDecimal economyClassCost) {
        super(id);
        this.allocatedSeats = notNull(allocatedSeats, "bookedSeats");
        this.airplaneProfile = notNull(airplaneProfile, "airplaneProfile");
        this.airline = notNull(airline, "airline");
        this.departure = notNull(departure, "departure");
        this.arrival = notNull(arrival, "arrival");
        this.firstClassCost = notNull(firstClassCost, "firstClassCost");
        this.businessClassCost = notNull(businessClassCost, "businessClassCost");
        this.economyClassCost = notNull(economyClassCost, "economyClassCost");

        if(seats == null) {
            List<FlightSeat> built = airplaneProfile.getFlightSeats(this);
            this.seats = BaseValueHolder.of(built);
        } else {
            this.seats = seats;
        }

        this.origin = notNull(origin, "origin");
        this.destination = notNull(destination, "destination");

        notNull(stopOvers, "stopOvers");
        this.stopOvers = new ArrayList<>();
        stopOvers.forEach(this::addStopOver);

        this.code = notNull(code, "code");
        this.status = notNull(status, "status");
    }

    public Flight(AirplaneProfile airplaneProfile, Airline airline, Airport origin, Airport destination, List<StopOver> stopOvers, String code, OffsetDateTime departure, OffsetDateTime arrival, BigDecimal firstClassCost, BigDecimal businessClassCost, BigDecimal economyClassCost) {
        this(EntityId.nextId(), ArrayList::new, airplaneProfile, airline, null, origin, destination, departure, arrival, stopOvers, code, Status.TO_SCHEDULE, firstClassCost, businessClassCost, economyClassCost);
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

    public Set<FlightSeat> getSeats() {
        return new HashSet<>(seats.get());
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

    public void addStopOver(Airport location, OffsetDateTime arrival, OffsetDateTime departure) {
        addStopOver(new StopOver(location, arrival, departure));
    }

    private void addStopOver(StopOver stopOver) {
        validateStopOver(stopOver);
        stopOvers.add(stopOver);
    }

    public List<FlightSeatAllocation> getAllocatedSeats() {
        return Collections.unmodifiableList(allocatedSeats.get());
    }

    private void validateStopOver(StopOver stopOver) {
        if (stopOver.getArrival().isBefore(departure)) {
            throw new ValueViolation("stopover arrival must not be before flight departure");
        }

        if (stopOver.getDeparture().isAfter(arrival)) {
            throw new ValueViolation("stopover departure must not be after flight arrival");
        }

        stopOvers.forEach(s -> {
            if (stopOver.getArrival().isAfter(s.getArrival()) && stopOver.getArrival().isBefore(s.getDeparture()) ||
                    stopOver.getDeparture().isAfter(s.getArrival()) && stopOver.getDeparture().isBefore(s.getDeparture()))
            {
                throw new ValueViolation("conflicting stopovers");
            }
        });
    }

    public enum Status {
        CANCELLED,
        TO_SCHEDULE,
        DELAYED,
    }
}
