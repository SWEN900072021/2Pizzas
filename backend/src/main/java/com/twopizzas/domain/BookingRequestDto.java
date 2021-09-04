package com.twopizzas.domain;

import java.util.List;

public class BookingRequestDto {
    private String flightId;
    private String returnId;
    private String customerId;
    private List<PassengerBooking> passengerBookings;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String fightId) {
        this.flightId = fightId;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<PassengerBooking> getPassengerBookings() {
        return passengerBookings;
    }

    public void setPassengerBookings(List<PassengerBooking> passengerBookings) {
        this.passengerBookings = passengerBookings;
    }

    public static class PassengerBooking {
        private String name;
        private String passport;
        private List<SeatAllocationDto> seatAllocations;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassport() {
            return passport;
        }

        public void setPassport(String passport) {
            this.passport = passport;
        }

        public List<SeatAllocationDto> getSeatAllocations() {
            return seatAllocations;
        }

        public void setSeatAllocations(List<SeatAllocationDto> seatAllocations) {
            this.seatAllocations = seatAllocations;
        }
    }

    public static class SeatAllocationDto {
        private String flightId;
        private String seatName;

        public String getFlightId() {
            return flightId;
        }

        public void setFlightId(String flightId) {
            this.flightId = flightId;
        }

        public String getSeatName() {
            return seatName;
        }

        public void setSeatName(String seatName) {
            this.seatName = seatName;
        }
    }
}
