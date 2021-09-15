package com.twopizzas.domain.flight;

import com.twopizzas.util.AssertionConcern;

import java.util.List;
import java.util.stream.Collectors;


class SeatProfile extends AssertionConcern {
    private static final List<Character> VALID_COLUMNS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().mapToObj(e -> (char) e).collect(Collectors.toList());
    private final int rowId;
    private final int columnId;
    private final SeatClass seatClass;

    public SeatProfile(int rowId, int columnId, SeatClass seatClass) {
        this.rowId = min(rowId, 0, "rowId");
        this.columnId = min(max(columnId, VALID_COLUMNS.size(), "columnId"), 0, "columnId");
        this.seatClass = seatClass;
    }

    String getName() {
        return String.format("%s%s", rowId + 1, VALID_COLUMNS.get(columnId));
    }

    SeatClass getSeatClass() {
        return seatClass;
    }
}
