package com.twopizzas.port.data.flight;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.booking.TimePeriod;
import com.twopizzas.domain.flight.Flight;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchSpecification implements FlightSpecification {

    private static final String TEMPLATE = "SELECT * from flight";

    private final FlightMapper flightMapper;

    private final EntityId airlineId;
    private final EntityId originId;
    private final EntityId destinationId;
    private final TimePeriod departing;

    public FlightSearchSpecification(FlightMapper flightMapper, EntityId airlineId, EntityId originId, EntityId destinationId, TimePeriod departing) {
        this.flightMapper = flightMapper;
        this.airlineId = airlineId;
        this.originId = originId;
        this.destinationId = destinationId;
        this.departing = departing;
    }

    @Override
    public List<Flight> execute(ConnectionPool context) {
        return new SqlStatement(getTemplate(), getParameters()).doQuery(context.getCurrentTransaction(), flightMapper);
    }

    private String getTemplate() {
        List<String> where = new ArrayList<>();
        if (airlineId != null) {
            where.add("airlineId = ?");
        }

        if (originId != null) {
            where.add("origin = ?");
        }

        if (destinationId != null) {
            where.add("destination = ?");
        }

        if (departing != null) {
            where.add("departure BETWEEN ? AND ?");
        }

        if (where.size() > 0) {
            return String.format("%s WHERE %s;", TEMPLATE, String.join(" AND ", where));
        }
        return String.format("%s;", TEMPLATE);
    }

    private Object[] getParameters() {
        List<Object> where = new ArrayList<>();
        if (airlineId != null) {
            where.add(airlineId.getValue().toString());
        }

        if (originId != null) {
            where.add(originId.getValue().toString());
        }

        if (destinationId != null) {
            where.add(destinationId.getValue().toString());
        }

        if (departing != null) {
            where.add(departing.getStart());
            where.add(departing.getEnd());
        }

        return where.toArray();
    }
}
