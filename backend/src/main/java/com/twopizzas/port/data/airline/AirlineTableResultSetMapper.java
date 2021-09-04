package com.twopizzas.port.data.airline;

import com.twopizzas.domain.Airline;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirlineTableResultSetMapper implements SqlResultSetMapper<Airline> {
    @Override
    public List<Airline> map(ResultSet resultSet) {
       List<Airline> mapped = new ArrayList<>();
       try {
           while (resultSet.next()) {
               mapped.add(new Airline(
                       EntityId.of(resultSet.getObject(AirlineMapperImpl.COLUMN_ID, String.class)),
                       resultSet.getObject(UserMapperImpl.COLUMN_USERNAME, String.class),
                       resultSet.getObject(UserMapperImpl.COLUMN_PASSWORD, String.class),
                       resultSet.getObject(UserMapperImpl.COLUMN_TYPE, String.class),
                       resultSet.getObject(AirlineMapperImpl.COLUMN_CODE, String.class),
                       resultSet.getObject(AirlineMapperImpl.COLUMN_NAME, String.class)
               ));
           }
       } catch (SQLException e) {
           throw new DataMappingException(String.format(
                   "failed to map results from result set to %s entity, error: %s", Airline.class.getName(), e.getMessage()),
                   e);
       }
       return mapped;
    }
}
