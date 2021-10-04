package com.twopizzas.api.booking;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Controller;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.domain.booking.BookingRepository;
import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.domain.booking.PassengerRepository;
import com.twopizzas.domain.error.BusinessRuleException;
import com.twopizzas.domain.error.NotFoundException;
import com.twopizzas.domain.flight.BookingRequest;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.domain.flight.FlightRepository;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.domain.user.Customer;
import com.twopizzas.domain.user.User;
import com.twopizzas.web.*;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookingController {

    private static final BookingMapper MAPPER = Mappers.getMapper(BookingMapper.class);
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;

    @Autowired
    public BookingController(FlightRepository repository, BookingRepository bookingRepository, PassengerRepository passengerRepository) {
        this.flightRepository = repository;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
    }

    @RequestMapping(path = "/customer/booking", method = HttpMethod.GET)
    @Authenticated(Customer.TYPE)
    RestResponse<List<BookingDto>> getCustomerBookings(User authenticatedUser) {
        return RestResponse.ok(bookingRepository.findAllCustomerBookings(authenticatedUser.getId()).stream().map(MAPPER::map).collect(Collectors.toList()));
    }

    @RequestMapping(path = "/booking", method = HttpMethod.POST)
    @Authenticated(Customer.TYPE)
    RestResponse<BookingDto> createBooking(@RequestBody NewBookingDto body, User authenticatedUser) throws HttpException {
        List<String> errors = body.validate();
        if (!errors.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }
        Flight flight = flightRepository.find(EntityId.of(body.getFlightId())).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("flight %s not found", body.getFlightId())));
        Flight returnFlight = null;
        if (body.getReturnFlightId() != null) {
            returnFlight = flightRepository.find(EntityId.of(body.getReturnFlightId())).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("flight %s not found", body.getFlightId())));
        }

        Customer customer = (Customer) authenticatedUser;
        Booking booking = new Booking(EntityId.nextId(), OffsetDateTime.now().withNano(0), customer, 0);
        bookingRepository.save(booking);

        BookingRequest.BookingRequestBuilder flightBuilder = BookingRequest.builder();
        BookingRequest.BookingRequestBuilder returnBuilder = BookingRequest.builder();
        body.getPassengerBookings().forEach(
                passengerBooking -> {
                    Passenger passenger = new Passenger(
                            passengerBooking.getGivenName(),
                            passengerBooking.getSurname(),
                            passengerBooking.getDateOfBirth(),
                            passengerBooking.getNationality(),
                            passengerBooking.getPassportNumber()
                    );
                    passenger.setBooking(booking);
                    passengerRepository.save(passenger);
                    passengerBooking.getSeatAllocations().forEach(a -> {

                        if (a.getFlightId().equals(body.getFlightId())) {
                            flightBuilder.withSeatAllocation(a.getSeatName(), passenger);
                        }

                        if (a.getFlightId().equals(body.getReturnFlightId())) {
                            returnBuilder.withSeatAllocation(a.getSeatName(), passenger);
                        }
                    });
                }
        );

        try {
            BookingRequest flightBooking = flightBuilder.build();
            if (flightBooking.getAllocations().isEmpty()) {
                throw new HttpException(HttpStatus.BAD_REQUEST, String.format("no seat allocations provided for flight %s", flight.getId()));
            }
            SeatBooking flightSeatBooking = flight.allocateSeats(flightBuilder.build());
            flightRepository.save(flight);

            SeatBooking returnSeatBooking = null;
            if (returnFlight != null) {
                BookingRequest returnFlightBooking = returnBuilder.build();
                if (returnFlightBooking.getAllocations().isEmpty()) {
                    throw new HttpException(HttpStatus.BAD_REQUEST, String.format("no seat allocations provided for return flight %s", returnFlight.getId()));
                }
                returnSeatBooking = returnFlight.allocateSeats(returnFlightBooking);
                flightRepository.save(returnFlight);
            }

            booking.addFlight(flightSeatBooking);
            booking.addReturnFlight(returnSeatBooking);
        } catch (BusinessRuleException e) {
            throw new HttpException(HttpStatus.CONFLICT, e.getMessage());
        }

        bookingRepository.save(booking);
        return RestResponse.ok(MAPPER.map(booking));
    }
}
