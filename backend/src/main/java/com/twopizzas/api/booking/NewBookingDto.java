package com.twopizzas.api.booking;

import com.twopizzas.api.ValidationUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
public class NewBookingDto {
    private String flightId;
    private String returnFlightId;
    private List<PassengerBooking> passengerBookings;

    @Data
    public static class PassengerBooking {
        private String givenName;
        private String surname;
        private String passportNumber;
        private LocalDate dateOfBirth;
        private String nationality;
        private List<SeatAllocation> seatAllocations;
    }

    @Data
    public static class SeatAllocation {
        private String flightId;
        private String seatName;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (!ValidationUtils.isUUID(flightId)) {
            errors.add("flightId must be a uuid");
        }

        if (returnFlightId != null && !ValidationUtils.isUUID(returnFlightId)) {
            errors.add("returnFlightId must be a uuid");
        }

        if (passengerBookings == null) {
            errors.add("passengerBookings is required");
        } else if (passengerBookings.isEmpty()) {
            errors.add("passengerBookings must not be empty");
        } else {
            for (int i = 0; i < passengerBookings.size(); i++) {
                PassengerBooking passengerBooking = passengerBookings.get(0);
                String passengerBookingPath = String.format("passengerBookings[%s]", i);
                if (passengerBooking.getDateOfBirth() == null) {
                    errors.add(passengerBookingPath + ".dateOfBirth is required");
                } else if (passengerBooking.getDateOfBirth().isAfter(LocalDate.now())) {
                    errors.add(passengerBookingPath + ".dateOfBirth must not be before current date"); // lol
                }

                if (passengerBooking.getGivenName() == null || passengerBooking.getGivenName().trim().isEmpty()) {
                    errors.add(passengerBookingPath + ".givenName must not be blank");
                }

                if (passengerBooking.getSurname() == null || passengerBooking.getSurname().trim().isEmpty()) {
                    errors.add(passengerBookingPath + ".surname must not be blank");
                }

                if (passengerBooking.getNationality() == null || passengerBooking.getNationality().trim().isEmpty()) {
                    errors.add(passengerBookingPath + ".nationality must not be blank");
                }

                if (passengerBooking.getPassportNumber() == null || passengerBooking.getPassportNumber().trim().isEmpty()) {
                    errors.add(passengerBookingPath + ".passportNumber must not be blank");
                }

                if (passengerBooking.getSeatAllocations() == null) {
                    errors.add(passengerBookingPath + ".seatAllocations is required");
                } else if (passengerBooking.getSeatAllocations().isEmpty()) {
                    errors.add(passengerBookingPath + ".seatAllocations must not be empty");
                } else {
                    for (int j = 0; j < passengerBooking.getSeatAllocations().size(); j++) {
                        SeatAllocation seatAllocation = passengerBooking.getSeatAllocations().get(j);
                        String seatAllocationPath = String.format("%s.seatAllocations[%s]", passengerBookingPath, i);

                        if (seatAllocation.getSeatName() == null || seatAllocation.getSeatName().trim().isEmpty()) {
                            errors.add(seatAllocationPath + ".seatName must not be blank");
                        }

                        if (!ValidationUtils.isUUID(seatAllocation.getFlightId())) {
                            errors.add(seatAllocationPath + ".flightId must be a uuid");
                        } else if (!(seatAllocation.getFlightId().equals(flightId) || seatAllocation.getFlightId().equals(returnFlightId))) {
                            errors.add(seatAllocationPath + ".flightId must match either flightId or returnFlightId");
                        }
                    }

                }
            }
        }
        return errors;
    }
}
