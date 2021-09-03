package com.twopizzas.port.data.airplane;

import com.twopizzas.domain.AirplaneProfile;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirplaneTableResultSetMapper implements SqlResultSetMapper<Airplane> {
    public List<Airplane> map(ResultSet resultSet) {
        List<Airplane> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                mapped.add(new Airplane(
                        EntityId.of(resultSet.getObject(AirplaneMapperImpl.COLUMN_ID, String.class)),
                        resultSet.getObject(AirplaneMapperImpl.COLUMN_CODE, String.class),
                        resultSet.getObject(AirplaneMapperImpl.COLUMN_NAME, String.class),
                        resultSet.getObject(AirplaneMapperImpl.COLUMN_LOCATION, String.class),
                        ZoneId.of(resultSet.getObject(AirplaneMapperImpl.COLUMN_UTC_OFFSET, String.class))
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Airplane.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }
}
