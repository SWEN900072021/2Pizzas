package com.twopizzas.port.data.passenger;

import com.twopizzas.data.BaseValueHolder;
import com.twopizzas.data.LazyValueHolderProxy;
import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingPassengersLoader implements LazyValueHolderProxy.ValueLoader<List<Passenger>> {

    private static final String TEMPLATE = "SELECT * FROM passenger JOIN seat ON  WHERE bookingId = ?";

    private final EntityId bookingId;
    private final ConnectionPool connection;

    private final PassengerTableResultSetMapper mapper = new PassengerTableResultSetMapper();

    public BookingPassengersLoader(EntityId bookingId, ConnectionPool connection) {
        this.bookingId = bookingId;
        this.connection = connection;
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
