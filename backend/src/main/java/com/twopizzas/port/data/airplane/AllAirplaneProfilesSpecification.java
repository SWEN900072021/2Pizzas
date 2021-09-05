package com.twopizzas.port.data.airplane;

import com.twopizzas.domain.flight.AirplaneProfile;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class AllAirplaneProfilesSpecification implements AirplaneProfileSpecification {

    private static final String TEMPLATE =
            "SELECT * FROM " + AirplaneProfileMapperImpl.TABLE_AIRPLANE + ";";

    private final AirplaneProfileMapper mapper;

    public AllAirplaneProfilesSpecification(AirplaneProfileMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<AirplaneProfile> execute(ConnectionPool context) {
        return mapper.map(new SqlStatement(TEMPLATE).doQuery(context.getCurrentTransaction()));
    }
}
