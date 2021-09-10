package com.twopizzas.port.data.passenger;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.data.ValueLoader;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.util.List;

public class BookingPassengersLoader implements ValueLoader<List<Passenger>> {

    private static final String TEMPLATE = "SELECT * FROM passenger JOIN booking ON passenger.bookingId = booking.id;";

    private final EntityId bookingId;
    private final ConnectionPool connection;
    private final PassengerMapper mapper;


    public BookingPassengersLoader(EntityId bookingId, ConnectionPool connection, PassengerMapper mapper) {
        this.bookingId = bookingId;
        this.connection = connection;
        this.mapper = mapper;
    }

    @Override
    public ValueHolder<List<Passenger>> load() {
        ResultSet resultSet = new SqlStatement(TEMPLATE, bookingId.toString()).doQuery(connection.getCurrentTransaction());
        return BaseValueHolder.of(mapResults(resultSet));
    }

    private List<Passenger> mapResults(ResultSet resultSet) {
        return mapper.map(resultSet);
    }
}
