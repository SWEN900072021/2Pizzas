package com.twopizzas.port.data.passenger;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.customer.CustomerTableResultSetMapper;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class PassengerMapperImpl implements PassengerMapper {

    static final String TABLE_PASSENGER = "passenger";
    static final String COLUMN_ID = "id";
    static final String COLUMN_GIVENNAME = "givenName";
    static final String COLUMN_SURNAME = "surname";
    static final String COLUMN_DOB = "dob";
    static final String COLUMN_NATIONALITY = "nationality";
    static final String COLUMN_PASSPORTNUMBER = "passportnumber";
    static final String COLUMN_BOOKINGID = "bookingid";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_PASSENGER +
                    "(" + COLUMN_ID + ", " + COLUMN_GIVENNAME + ", " + COLUMN_SURNAME + ", " + COLUMN_DOB + ", " +
                    COLUMN_NATIONALITY + ", " + COLUMN_PASSPORTNUMBER + ", " + COLUMN_BOOKINGID + ")" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_PASSENGER +
                    " SET " + COLUMN_GIVENNAME + " = ?, " + COLUMN_SURNAME + " = ?, " + COLUMN_DOB + " = ?, " +
                    COLUMN_NATIONALITY + " = ?, " + COLUMN_PASSPORTNUMBER + " = ?, " + COLUMN_BOOKINGID + " = ? " +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_PASSENGER +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_PASSENGER +
                    " WHERE id = ?;";

    private final PassengerTableResultSetMapper mapper = new PassengerTableResultSetMapper();
    private ConnectionPool connectionPool;

    @Autowired
    PassengerMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    @Override
    public void create(Passenger entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getGivenName(),
                entity.getLastName(),
                entity.getDateOfBirth(),
                entity.getNationality(),
                entity.getPassportNumber()).doExecute(connectionPool.getCurrentTransaction());

    }

    @Override
    public Passenger read(EntityId entityId) {
        List<Passenger> passengers = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), mapper);
        if (passengers.isEmpty()) {
            return null;
        }
        return passengers.get(0);
    }

    @Override
    public List<Passenger> readAll(PassengerSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Passenger entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getGivenName(),
                entity.getLastName(),
                entity.getDateOfBirth(),
                entity.getNationality(),
                entity.getPassportNumber(),
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Passenger entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }
}
