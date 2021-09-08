package com.twopizzas.port.data.seatallocation;

import com.twopizzas.data.LazyValueHolderProxy;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;
import com.twopizzas.port.data.booking.BookingByIdLoader;
import com.twopizzas.port.data.booking.BookingMapper;
import com.twopizzas.port.data.passenger.PassengerMapper;
import com.twopizzas.port.data.seat.FlightSeatMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlightSeatAllocationResultsMapperImpl implements FlightSeatAllocationResultsMapper {

    static final String TABLE_ALLOCATION = "seatAllocation";
    static final String COLUMN_PASSENGER_ID = "passengerId";
    static final String COLUMN_SEAT_ID = "seatId";
    static final String COLUMN_BOOKING_ID = "bookingId";

    private final PassengerMapper passengerMapper;
    private final FlightSeatMapper flightSeatMapper;

    @Autowired
    public FlightSeatAllocationResultsMapperImpl(PassengerMapper passengerMapper, FlightSeatMapper flightSeatMapper) {
        this.passengerMapper = passengerMapper;
        this.flightSeatMapper = flightSeatMapper;
    }

    @Override
    public List<FlightSeatAllocation> map(ResultSet resultSet) {
        List<FlightSeatAllocation> seatAllocations = new ArrayList<>();
        try {
            while (resultSet.next()) {
                seatAllocations.add(new FlightSeatAllocation(
                        flightSeatMapper.read(EntityId.of(resultSet.getObject(COLUMN_SEAT_ID, String.class))),
                        passengerMapper.read(EntityId.of(resultSet.getObject(COLUMN_PASSENGER_ID, String.class)))
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", FlightSeatAllocation.class.getName(), e.getMessage()),
                    e);
        }
        return seatAllocations;
    }
}
