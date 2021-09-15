package com.twopizzas.port.data.seatallocation;

import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.flight.FlightSeatAllocation;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.passenger.PassengerMapper;
import com.twopizzas.port.data.seat.FlightSeatMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlightSeatAllocationMapperImpl implements FlightSeatAllocationMapper {

    static final String COLUMN_PASSENGER_ID = "passengerId";
    static final String COLUMN_SEAT_ID = "seatId";

    private final PassengerMapper passengerMapper;
    private final FlightSeatMapper flightSeatMapper;

    @Autowired
    public FlightSeatAllocationMapperImpl(PassengerMapper passengerMapper, FlightSeatMapper flightSeatMapper) {
        this.passengerMapper = passengerMapper;
        this.flightSeatMapper = flightSeatMapper;
    }

    @Override
    public List<FlightSeatAllocation> map(ResultSet resultSet) {
        List<FlightSeatAllocation> seatAllocations = new ArrayList<>();
        try {
            while (resultSet.next()) {
                FlightSeatAllocation one = mapOne(resultSet);
                if (one != null) {
                    seatAllocations.add(one);
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", FlightSeatAllocation.class.getName(), e.getMessage()),
                    e);
        }
        return seatAllocations;
    }

    @Override
    public FlightSeatAllocation mapOne(ResultSet resultSet) {
        try {
            return new FlightSeatAllocation(
                    flightSeatMapper.read(EntityId.of(resultSet.getObject(COLUMN_SEAT_ID, String.class))),
                    passengerMapper.read(EntityId.of(resultSet.getObject(COLUMN_PASSENGER_ID, String.class)))
            );
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", FlightSeatAllocation.class.getName(), e.getMessage()),
                    e);
        }
    }
}
