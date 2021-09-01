package com.twopizzas.domain;

import com.twopizzas.util.ValueViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SeatTests {

    @Test
    @DisplayName("GIVEN valid seat data WHEN constructor invoked THEN seat returned")
    void test() {
        // GIVEN + WHEN
        Flight flight = Mockito.mock(Flight.class);
        Seat seat = new Seat(1, 'a', flight, Seat.SeatClass.BUSINESS);

        // THEN
        Assertions.assertEquals("1A", seat.getName());
        Assertions.assertEquals(flight, seat.getFlight());
        Assertions.assertFalse(seat.getBooking().isPresent());
        Assertions.assertEquals(Seat.SeatClass.BUSINESS, seat.getSeatClass());
    }

    @Test
    @DisplayName("GIVEN invalid seat columnLetter data WHEN constructor invoked THEN throws")
    void test2() {
        // GIVEN + WHEN + THEN
        Flight flight = Mockito.mock(Flight.class);
        Assertions.assertThrows(ValueViolation.class, () -> new Seat(1, '2', flight, Seat.SeatClass.BUSINESS));
    }

    @Test
    @DisplayName("GIVEN invalid seat rowId data WHEN constructor invoked THEN throws")
    void test3() {
        // GIVEN + WHEN + THEN
        Flight flight = Mockito.mock(Flight.class);
        Assertions.assertThrows(ValueViolation.class, () -> new Seat(0, 'a', flight, Seat.SeatClass.BUSINESS));
    }

    @Test
    @DisplayName("GIVEN null flight data WHEN constructor invoked THEN throws")
    void test4() {
        // GIVEN + WHEN + THEN
        Assertions.assertThrows(ValueViolation.class, () -> new Seat(0, 'a', null, Seat.SeatClass.BUSINESS));
    }
}
