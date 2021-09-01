package com.twopizzas.port.data.booking;

import com.twopizzas.data.DataMapper;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.db.SqlConnectionPool;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class BookingMapper implements DataMapper<Booking, EntityId, BookingSpecification> {

    private static final String createStatementString =
        "INSERT INTO booking(id, date, totalcost, reference) " +
        " VALUES (?, ?, ?, ?, ?);";

    private static final String updateStatementString =
        "UPDATE booking " +
        " SET date = ?, date = ?, totalcost = ?, reference = ? " +
        " WHERE id = ?;";

    private static final String deleteStatementString =
        "DELETE FROM booking " +
        " WHERE id = ?;";

    private SqlConnectionPool connectionPool;

    @Autowired
    BookingMapper(SqlConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Booking create(Booking entity) {
        PreparedStatement createStatement = null;

        try {
            createStatement = connectionPool.getCurrentTransaction().prepareStatement(createStatementString);

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
            updateStatement = connectionPool.getCurrentTransaction().prepareStatement(updateStatementString);

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
            deleteStatement = connectionPool.getCurrentTransaction().prepareStatement(deleteStatementString);
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
