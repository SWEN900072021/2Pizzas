package com.twopizzas.port.data.booking;

import com.twopizzas.data.DataMapper;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class BookingMapper implements DataMapper<Booking, EntityId, BookingSpecification> {

    // r = getConnection
    // cud = getCurrentTransaction

    private static final String createStatementString =
        "INSERT INTO booking(id, date, time, totalcost, bookingreference) " +
        " VALUES (?, ?, ?, ?, ?);";

    private static final String updateStatementString =
        "UPDATE booking " +
        " SET date = ?, time = ?, totalcost = ?, bookingreference = ? " +
        " WHERE id = ?;";

    private static final String deleteStatementString =
        "DELETE FROM booking " +
        " WHERE id = ?;";

    @Override
    public Booking create(Booking entity) {
        PreparedStatement createStatement = null;

        try {
            createStatement = ConnectionPool.getCurrentTransaction().prepareStatement(createStatementString);
            createStatement.setString(1, entity.getId().toString());
            createStatement.setDate(2, entity.getDate());
            createStatement.setTime(3, entity.getTime());
            createStatement.setString(4, entity.getTotalCost());
            createStatement.setString(5, entity.getBookingReference());
            createStatement.execute();
        } catch (SQLException e) {
        }

        // change this
        return entity;
    }

    @Override
    public Booking read(EntityId entityId) {
        return null;
    }

    @Override
    public List<Booking> readAll(BookingSpecification specification) {
        return null;
    }

    @Override
    public Booking update(Booking entity) {
        PreparedStatement updateStatement = null;

        try {
            updateStatement = ConnectionPool.getCurrentTransaction().prepareStatement(updateStatementString);
            updateStatement.setDate(1, entity.getDate());
            updateStatement.setTime(2, entity.getTime());
            updateStatement.setString(3, entity.getTotalCost());
            updateStatement.setString(4, entity.getBookingReference());
            updateStatement.setString(5, entity.getId().toString());
            updateStatement.execute();
        } catch (SQLException e) {
        }

        // change this
        return  entity;
    }

    @Override
    public void delete(Booking entity) {
        PreparedStatement deleteStatement = null;

        try {
            deleteStatement = ConnectionPool.getCurrentTransaction().prepareStatement(deleteStatementString);
            deleteStatement.setString(1, entity.getId().toString());
            deleteStatement.execute();
        } catch (SQLException e) {
        }
    }

    @Override
    public Class<Booking> getEntityClass() {
        return Booking.class;
    }
}
