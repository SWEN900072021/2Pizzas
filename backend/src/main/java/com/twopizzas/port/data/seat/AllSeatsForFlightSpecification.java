package com.twopizzas.port.data.seat;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeat;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AllSeatsForFlightSpecification implements FlightSeatSpecification {

    private static final String TEMPLATE =
            "SELECT * FROM " + FlightSeatMapperImpl.TABLE_SEAT +
                    " WHERE " + FlightSeatMapperImpl.COLUMN_FLIGHT_ID + " = ?;";

    private final FlightSeatMapper mapper;
    private final EntityId flightId;

    public AllSeatsForFlightSpecification(FlightSeatMapper mapper, EntityId flightId) {
        this.mapper = mapper;
        this.flightId = flightId;
    }

    @Override
    public List<FlightSeat> execute(ConnectionPool context) {
        return mapper.map(new SqlStatement(TEMPLATE,
                flightId.toString()
                ).doQuery(context.getCurrentTransaction()));
    }
}
