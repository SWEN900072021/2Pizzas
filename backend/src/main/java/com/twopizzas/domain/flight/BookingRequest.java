package com.twopizzas.domain.flight;

import com.twopizzas.domain.booking.Passenger;
import com.twopizzas.util.AssertionConcern;
import lombok.Getter;

import java.util.List;

@Getter
public class BookingRequest extends AssertionConcern {

    private final List<SeatAllocationRequest> allocations;

    public BookingRequest(List<SeatAllocationRequest> allocations) {
        this.allocations = notEmpty(allocations, "allocations");
    }

    public static BookingRequestBuilder builder() {
        return new BookingRequestBuilder();
    }

    public static class BookingRequestBuilder {
        private List<SeatAllocationRequest> allocations;

        public BookingRequestBuilder withSeatAllocation(String seatName, Passenger passenger) {
            allocations.add(new SeatAllocationRequest(seatName, passenger));
            return this;
        }

        public BookingRequest build() {
            return new BookingRequest(allocations);
        }
    }

    @Getter
    public static class SeatAllocationRequest extends AssertionConcern{
        private final String seatName;
        private final Passenger passenger;

        public SeatAllocationRequest(String seatName, Passenger passenger) {
            this.seatName = notNullAndNotBlank(seatName, "seatName");
            this.passenger = notNull(passenger, "passenger");
        }
    }

}
