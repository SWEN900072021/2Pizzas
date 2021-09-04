package com.twopizzas.domain.flight;

import com.twopizzas.domain.Passenger;

import java.util.List;

public class BookingRequest {
    List<SeatAllocationRequest> allocationRequests;

    public BookingRequestBuilder builder() {
        return new BookingRequestBuilder();
    }

    public static class BookingRequestBuilder {
        private List<SeatAllocationRequest> allocations;

        public BookingRequestBuilder withSeatAllocation(String seatName, Passenger passenger) {
            allocations.add(new SeatAllocationRequest(seatName, passenger));
            return this;
        }
    }

    public static class SeatAllocationRequest {
        private final String seatName;
        private final Passenger passenger;

        public SeatAllocationRequest(String seatName, Passenger passenger) {
            this.seatName = seatName;
            this.passenger = passenger;
        }
    }

}
