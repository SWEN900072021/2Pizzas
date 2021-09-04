package com.twopizzas.domain.flight;

import com.twopizzas.domain.Passenger;

import java.util.List;

public class SeatAllocation {
    List<Allocation> allocations;
    Flight flight;

    static class Allocation {
        FlightSeat seat;
        Passenger passenger;
    }

}
