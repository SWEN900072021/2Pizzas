package com.twopizzas.port.data.booking;

import com.twopizzas.data.ValueHolder;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingTableResultSetMapper implements SqlResultSetMapper<Booking> {
    @Override
    public List<Booking> map(ResultSet resultSet) {

        List<Booking> mapped = new ArrayList<>();

        try {

            // TODO: retrieve list of passengers
            ValueHolder<List<Passenger>> passengers = null;

            while (resultSet.next()) {
                mapped.add(new Booking(
                        EntityId.of(resultSet.getObject(BookingMapperImpl.COLUMN_ID, String.class)),
                        passengers,
                        resultSet.getObject(BookingMapperImpl.COLUMN_DATE, LocalDateTime.class),
                        resultSet.getObject(BookingMapperImpl.COLUMN_TOTALCOST, Double.class),
                        resultSet.getObject(BookingMapperImpl.COLUMN_REFERENCE, String.class),
                        flightBooking, returnFlightBooking));
            }
         } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Booking.class.getName(), e.getMessage()),
                    e);
        }

        return mapped;
    }
}
