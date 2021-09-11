package com.twopizzas.port.data.passenger;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.Passenger;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.booking.BookingMapper;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PassengerMapperImpl implements PassengerMapper {

    static final String TABLE_PASSENGER = "passenger";
    static final String COLUMN_ID = "id";
    static final String COLUMN_GIVENNAME = "givenName";
    static final String COLUMN_SURNAME = "surname";
    static final String COLUMN_DOB = "dob";
    static final String COLUMN_NATIONALITY = "nationality";
    static final String COLUMN_PASSPORTNUMBER = "passportnumber";
    static final String COLUMN_BOOKING_ID = "bookingid";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_PASSENGER +
                    "(" + COLUMN_ID + ", " + COLUMN_GIVENNAME + ", " + COLUMN_SURNAME + ", " + COLUMN_DOB + ", " +
                    COLUMN_NATIONALITY + ", " + COLUMN_PASSPORTNUMBER + ", " + COLUMN_BOOKING_ID + ")" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_PASSENGER +
                    " SET " + COLUMN_GIVENNAME + " = ?, " + COLUMN_SURNAME + " = ?, " + COLUMN_DOB + " = ?, " +
                    COLUMN_NATIONALITY + " = ?, " + COLUMN_PASSPORTNUMBER + " = ?, " + COLUMN_BOOKING_ID + " = ? " +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_PASSENGER +
                    " WHERE id = ?;";

    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_PASSENGER +
                    " WHERE id = ?;";

    private ConnectionPool connectionPool;

    private final BookingMapper bookingMapper;

    @Autowired
    PassengerMapperImpl(ConnectionPool connectionPool, BookingMapper bookingMapper) {
        this.connectionPool = connectionPool;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public void create(Passenger entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getGivenName(),
                entity.getSurname(),
                entity.getDateOfBirth(),
                entity.getNationality(),
                entity.getPassportNumber(),
                entity.getBooking().getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());

    }

    @Override
    public Passenger read(EntityId entityId) {
        return map(new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction()))
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public List<Passenger> readAll(PassengerSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Passenger entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getGivenName(),
                entity.getSurname(),
                entity.getDateOfBirth(),
                entity.getNationality(),
                entity.getPassportNumber(),
                entity.getBooking().getId().toString(),
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Passenger entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public List<Passenger> map(ResultSet resultSet) {

        List<Passenger> mapped = new ArrayList<>();
        Passenger one;

        try {
            while(resultSet.next()) {
                one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
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
        try {
            return new Passenger(
                    EntityId.of(resultSet.getObject(COLUMN_ID, String.class)),
                    resultSet.getObject(COLUMN_GIVENNAME, String.class),
                    resultSet.getObject(COLUMN_SURNAME, String.class),
                    resultSet.getObject(COLUMN_DOB, LocalDate.class),
                    resultSet.getObject(COLUMN_NATIONALITY, String.class),
                    resultSet.getObject(COLUMN_PASSPORTNUMBER, String.class),
                    bookingMapper.read(EntityId.of(resultSet.getObject(COLUMN_BOOKING_ID, String.class)))
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
