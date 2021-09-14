package com.twopizzas.port.data.booking;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.booking.Booking;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.SeatBooking;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.customer.CustomerMapper;
import com.twopizzas.port.data.db.ConnectionPool;
import com.twopizzas.port.data.flight.FlightMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationMapper;
import com.twopizzas.port.data.seatallocation.FlightSeatAllocationsForFlightBookingLoader;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BookingMapperImpl implements BookingMapper {

    static final String TABLE_BOOKING = "booking";
    static final String COLUMN_ID = "id";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_TOTALCOST = "totalcost";
    static final String COLUMN_CUSTOMER_ID = "customerid";
    static final String COLUMN_FLIGHT_ID = "flightid";
    static final String COLUMN_RETURNFLIGHT_ID = "returnflightid";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_BOOKING + "(" + COLUMN_ID + ", " + COLUMN_DATE + ", " + COLUMN_TOTALCOST + ", " + COLUMN_CUSTOMER_ID + ", " + COLUMN_FLIGHT_ID + ", " + COLUMN_RETURNFLIGHT_ID + ")" +
                    " VALUES (?, ?, ?, ?, ? ,?);";
    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_BOOKING +
                    " SET " + COLUMN_DATE + " = ?, " + COLUMN_TOTALCOST + " = ?, " + COLUMN_CUSTOMER_ID + " = ?, " + COLUMN_FLIGHT_ID + " = ?, " + COLUMN_RETURNFLIGHT_ID + " = ? " +
                    " WHERE id = ?;";
    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_BOOKING +
                    " WHERE id = ?;";
    private static final String SELECT_TEMPLATE =
            "SELECT * FROM " + TABLE_BOOKING +
                    " WHERE id = ?;";

    private final ConnectionPool connectionPool;

    private final CustomerMapper customerMapper;
    private final FlightMapper flightMapper;
    private final FlightSeatAllocationMapper flightSeatAllocationResultsMapper;

    @Autowired
    BookingMapperImpl(ConnectionPool connectionPool,
                      CustomerMapper customerMapper,
                      FlightMapper flightMapper,
                      FlightSeatAllocationMapper flightSeatAllocationResultsMapper
                      ) {
        this.connectionPool = connectionPool;
        this.customerMapper = customerMapper;
        this.flightMapper = flightMapper;
        this.flightSeatAllocationResultsMapper = flightSeatAllocationResultsMapper;
    }

    @Override
    public void create(Booking entity) {

        EntityId bookingId = entity.getId();
        OffsetDateTime date = entity.getDate();
        BigDecimal totalCost = entity.getTotalCost();
        EntityId customerId = entity.getCustomer().getId();
        EntityId flightId = entity.getFlightReservation().getFlight().getId();
        String returnId = entity.getReturnFlightReservation() == null ? null : entity.getReturnFlightReservation().getFlight().getId().toString();

        new SqlStatement(CREATE_TEMPLATE,
                bookingId.toString(),
                date,
                totalCost,
                customerId.toString(),
                flightId.toString(),
                returnId
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public Booking read(EntityId entityId) {
        return map(new SqlStatement(SELECT_TEMPLATE, entityId.toString())
                .doQuery(connectionPool.getCurrentTransaction()))
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public List<Booking> readAll(BookingSpecification specification) {
        return specification.execute(connectionPool);
    }

    @Override
    public void update(Booking entity) {
        EntityId bookingId = entity.getId();
        OffsetDateTime date = entity.getDate();
        BigDecimal totalCost = entity.getTotalCost();
        EntityId customerId = entity.getCustomer().getId();
        EntityId flightId = entity.getFlightReservation().getFlight().getId();
        String returnId = entity.getReturnFlightReservation() == null ? null : entity.getReturnFlightReservation().getFlight().getId().toString();

        new SqlStatement(UPDATE_TEMPLATE,
                date,
                totalCost,
                customerId.toString(),
                flightId.toString(),
                returnId,
                bookingId.toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public void delete(Booking entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()).doExecute(connectionPool.getCurrentTransaction());
    }

    @Override
    public List<Booking> map(ResultSet resultSet) {

        List<Booking> mapped = new ArrayList<>();
        Booking one;

        try {
            while (resultSet.next()) {
                one = mapOne(resultSet);
                if (one != null) {
                    mapped.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Booking.class.getName(), e.getMessage()),
                    e);
        }

        return mapped;

    }

    @Override
    public Booking mapOne(ResultSet resultSet) {
        try {

            EntityId bookingId = EntityId.of(resultSet.getObject(COLUMN_ID, String.class));

            Booking one = new Booking(
                    bookingId,
                    resultSet.getObject(COLUMN_DATE, OffsetDateTime.class),
                    resultSet.getObject(COLUMN_TOTALCOST, BigDecimal.class),
                    customerMapper.read(EntityId.of(resultSet.getObject(COLUMN_CUSTOMER_ID, String.class)))
            );

            EntityId flightId = EntityId.of(resultSet.getObject(COLUMN_FLIGHT_ID, String.class));
            one.addFlight(new SeatBooking(
                    flightMapper.read(flightId),
                    new HashSet<>(new FlightSeatAllocationsForFlightBookingLoader(
                            connectionPool,
                            flightSeatAllocationResultsMapper,
                            flightId,
                            bookingId).load().get())
            ));

            String returnId =  resultSet.getObject(COLUMN_RETURNFLIGHT_ID, String.class);

            if (returnId != null) {
                EntityId returnFlightId = EntityId.of(returnId);
                one.addReturnFlight(new SeatBooking(
                        flightMapper.read(returnFlightId),
                        new HashSet<>(new FlightSeatAllocationsForFlightBookingLoader(
                                connectionPool,
                                flightSeatAllocationResultsMapper,
                                returnFlightId,
                                bookingId).load().get())
                ));
            }

            return one;

        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Booking.class.getName(), e.getMessage()),
                    e);
        }
    }
}
