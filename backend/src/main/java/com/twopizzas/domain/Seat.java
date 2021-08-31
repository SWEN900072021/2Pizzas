package com.twopizzas.domain;

import com.twopizzas.data.ValueHolder;

public class Seat {

    private final String sequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int rowId;
    private int columnId;
    private ValueHolder<Flight> flight;
    private ValueHolder<Booking> booking;
    private SeatClass seatClass;

    public String getName() {

        assert 1 <= columnId && columnId <= 26;

        return String.format("%s%s", Integer.toString(rowId), sequence.charAt(rowId - 1));
    }

    public enum SeatClass {
        FIRST, BUSINESS, ECONOMY,
    }

}
