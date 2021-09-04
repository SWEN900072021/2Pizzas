package com.twopizzas.port.data.booking;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookingMapperImpl implements BookingMapper{

    static final String TABLE_BOOKING = "booking";
    static final String COLUMN_ID = "id";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_TOTALCOST = "totalcost";
    static final String COLUMN_REFERENCE = "reference";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_BOOKING + "(" + COLUMN_ID + ", " + COLUMN_DATE + ", " + COLUMN_TOTALCOST + ", " + COLUMN_REFERENCE + ")" +
                    " VALUES (?, ?, ?, ?);";
    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_BOOKING +
                    " SET " + COLUMN_DATE + " = ?, " + COLUMN_TOTALCOST + " = ?, " + COLUMN_REFERENCE + " = ? " +
                    " WHERE id = ?;";
    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_BOOKING +
                    " WHERE id = ?;";
    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_BOOKING +
                    " WHERE id = ?;";

    private final BookingTableResultSetMapper mapper = new BookingTableResultSetMapper();
    private ConnectionPool connectionPool;

    @Autowired
    BookingMapperImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void create(Booking entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getDate(),
                entity.getTotalCost(),
                entity.getBookingReference()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public Booking read(EntityId entityId) {
        List<Booking> bookings = new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction(), mapper);
        if (bookings.isEmpty()) {
            return null;
        }
        return bookings.get(0);
    }

    @Override
    public List<Booking> readAll(BookingSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Booking entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getDate(),
                entity.getTotalCost(),
                entity.getBookingReference(),
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Booking entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }
}
