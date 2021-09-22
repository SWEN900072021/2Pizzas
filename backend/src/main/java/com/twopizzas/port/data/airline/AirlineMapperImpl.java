package com.twopizzas.port.data.airline;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.user.Airline;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.user.AbstractUserMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AirlineMapperImpl extends AbstractUserMapper<Airline> implements AirlineMapper {

    static final String TABLE_AIRLINE = "airline";
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_CODE = "code";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_AIRLINE + "(" + COLUMN_ID + " , " + COLUMN_NAME + ", " + COLUMN_CODE + ")" +
                    " VALUES (?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_AIRLINE +
                    " SET " + COLUMN_NAME + " = ?, " + COLUMN_CODE + " = ?" +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_USER + " INNER JOIN " + TABLE_AIRLINE +
                    " ON " + TABLE_USER + ".id =" + TABLE_AIRLINE + ".id" +
                    " WHERE " + TABLE_USER + ".id = ?;";

    private final ConnectionPool connectionPool;

    @Autowired
    public AirlineMapperImpl(ConnectionPool connectionPool) {
        super(connectionPool);
        this.connectionPool = connectionPool; }

    @Override
    public void create(Airline entity) {
        abstractCreate(entity);
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getCode(),
                entity.getName()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public Airline read(EntityId entityId) {
        return map(new SqlStatement(SELECT_TEMPLATE,
                entityId.toString()
        ).doQuery(connectionPool.getCurrentTransaction())).stream().findFirst().orElse(null);
    }

    @Override
    public List<Airline> readAll(AirlineSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Airline entity) {
        abstractUpdate(entity);
        new SqlStatement(UPDATE_TEMPLATE,
            entity.getCode(),
            entity.getName(),
            entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Airline entity) {
        abstractDelete(entity);
    }

    @Override
    public Class<Airline> getEntityClass() {
        return Airline.class;
    }

    @Override
    public List<Airline> map(ResultSet resultSet) {
        List<Airline> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Airline one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Airline.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }

    @Override
    public Airline mapOne(ResultSet resultSet) {
        try {
            return new Airline(
                    EntityId.of(resultSet.getObject(AirlineMapperImpl.COLUMN_ID, String.class)),
                    resultSet.getObject(AbstractUserMapper.COLUMN_USERNAME, String.class),
                    resultSet.getObject(AbstractUserMapper.COLUMN_PASSWORD, String.class),
                    resultSet.getObject(AirlineMapperImpl.COLUMN_CODE, String.class),
                    resultSet.getObject(AirlineMapperImpl.COLUMN_NAME, String.class),
                    User.Status.valueOf(resultSet.getObject(AbstractUserMapper.COLUMN_STATUS, String.class))
            );
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", getEntityClass().getName(), e.getMessage()),
                    e);
        }
    }
}
