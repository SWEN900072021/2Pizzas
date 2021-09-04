package com.twopizzas.port.data.airplane;

import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirplaneTableResultSetMapper implements SqlResultSetMapper<AirplaneProfile> {
    public List<AirplaneProfile> map(ResultSet resultSet) {
        List<AirplaneProfile> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {

            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", AirplaneProfile.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }
}
