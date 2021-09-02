package com.twopizzas.port.data.airport;

import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AirportTableResultSetMapper implements SqlResultSetMapper<Airport> {
    public List<Airport> map(ResultSet resultSet) {
        List<Airport> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                mapped.add(new Airport(
                        EntityId.of(resultSet.getObject(AirportMapperImpl.COLUMN_ID, String.class)),
                        resultSet.getObject(AirportMapperImpl.COLUMN_CODE, String.class),
                        resultSet.getObject(AirportMapperImpl.COLUMN_NAME, String.class),
                        resultSet.getObject(AirportMapperImpl.COLUMN_LOCATION, String.class),
                        ZoneId.of(resultSet.getObject(AirportMapperImpl.COLUMN_UTC_OFFSET, String.class))
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Airport.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }
}
