package com.twopizzas.port.data.airport;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Airport;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.AbstractSqlDataMapper;
import com.twopizzas.port.data.db.SqlConnectionPool;

import java.util.List;

@Component
class AirportMapperImpl extends AbstractSqlDataMapper<Airport, EntityId, AirportSpecification> implements AirportMapper {

    private static final String create =
            "INSERT INTO airport(id, date, totalcost, reference) " +
            " VALUES (?, ?, ?, ?, ?);";

    private static final String update =
            "UPDATE booking " +
            " SET date = ?, date = ?, totalcost = ?, reference = ? " +
            " WHERE id = ?;";

    private static final String delete =
            "DELETE FROM booking " +
            " WHERE id = ?;";

    @Autowired
    AirportMapperImpl(SqlConnectionPool connectionPool) {
        super(connectionPool);
    }

    @Override
    public Airport create(Airport entity) {
        doExecute(create,
                entity.getId().toString(),
                entity.
                );
        return entity;
    }

    @Override
    public Airport read(EntityId entityId) {
        return null;
    }

    @Override
    public List<Airport> readAll(AirportSpecification specification) {
        return null;
    }

    @Override
    public Airport update(Airport entity) {
        return null;
    }

    @Override
    public void delete(Airport entity) {

    }


}
