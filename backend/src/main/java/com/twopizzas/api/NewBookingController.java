package com.twopizzas.api;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.*;
import com.twopizzas.domain.error.DataFormatException;
import com.twopizzas.domain.error.NotFoundException;
import com.twopizzas.domain.flight.*;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.passenger.PassengerMapper;

@Component
public class NewBookingController {

    private final FlightRepository flightRepository;
    private final CustomerMapper customerMapper;
    private final BookingRepository bookingRepository;
    private final PassengerMapper passengerRepository;

    @Autowired
    public NewBookingController(FlightRepository repository, CustomerMapper customerMapper, BookingRepository bookingRepository, PassengerMapper passengerRepository) {
        this.flightRepository = repository;
        this.customerMapper = customerMapper;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
    }

    void createBooking(BookingRequestDto requestDto) {
        Flight flight = flightRepository.find(EntityId.of(requestDto.getFlightId())).orElseThrow(() -> new NotFoundException("flight", requestDto.getFlightId()));
        Flight returnFlight = flightRepository.find(EntityId.of(requestDto.getReturnId())).orElseThrow(() -> new NotFoundException("flight", requestDto.getFlightId()));

        Customer customer = customerMapper.read(EntityId.of(requestDto.getCustomerId()));

        BookingRequest.BookingRequestBuilder flightBuilder = BookingRequest.builder();
        BookingRequest.BookingRequestBuilder returnBuilder = BookingRequest.builder();
        requestDto.getPassengerBookings().forEach(
                passengerBooking -> {
                    Passenger passenger = new Passenger(
                            passengerBooking.getGivenName(),
                            passengerBooking.getLastName(),
                            passengerBooking.getDateOfBirth(),
                            passengerBooking.getNationality(),
                            passengerBooking.getPassport()
                    );
                    passengerRepository.create(passenger);
                    passengerBooking.getSeatAllocations().forEach(a -> {

                        if (a.getFlightId().equals(requestDto.getFlightId())) {
                            flightBuilder.withSeatAllocation(a.getSeatName(), passenger);
                            return;
                        }

                        if (a.getFlightId().equals(requestDto.getReturnId())) {
                            returnBuilder.withSeatAllocation(a.getSeatName(), passenger);
                            return;
                        }

                        throw new DataFormatException(String.format("unknown flight id %s", a.getFlightId()));
                    });
                }
        );

        SeatBooking flightSeatBooking = flight.allocateSeats(flightBuilder.build());
        SeatBooking returnSeatBooking = returnFlight.allocateSeats(returnBuilder.build());

        Booking booking = new Booking(customer);
        booking.addFlight(flightSeatBooking);
        booking.addReturnFlight(returnSeatBooking);
        bookingRepository.save(booking);
    }
}
