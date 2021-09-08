package com.twopizzas.port.data.passenger;

import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PassengerTableResultSetMapper implements SqlResultSetMapper<Passenger> {

    @Override
    public List<Passenger> map(ResultSet resultSet) {
        List<Passenger> mapped = new ArrayList<>();

        try {

            while (resultSet.next()) {
                mapped.add(new Passenger(
                        EntityId.of(resultSet.getObject(PassengerMapperImpl.COLUMN_ID, String.class)),
                        resultSet.getObject(PassengerMapperImpl.COLUMN_GIVENNAME, String.class),
                        resultSet.getObject(PassengerMapperImpl.COLUMN_SURNAME, String.class),
                        resultSet.getObject(PassengerMapperImpl.COLUMN_DOB, LocalDate.class),
                        resultSet.getObject(PassengerMapperImpl.COLUMN_NATIONALITY, String.class),
                        resultSet.getObject(PassengerMapperImpl.COLUMN_PASSPORTNUMBER, String.class)

                ));
            }

        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Passenger.class.getName(), e.getMessage()),
                    e);
        }

        return mapped;

    }

    @Override
    public Passenger mapOne(ResultSet resultSet) {
        return null;
    }
}
