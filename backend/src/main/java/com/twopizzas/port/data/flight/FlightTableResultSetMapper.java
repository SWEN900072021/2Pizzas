package com.twopizzas.port.data.flight;

import com.twopizzas.domain.flight.Flight;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightTableResultSetMapper implements SqlResultSetMapper<Flight> {

    @Override
    public List<Flight> map(ResultSet resultSet) {
        return null;
    }

}
