package com.twopizzas.port.data.booking;

import com.twopizzas.data.DataMapper;
import com.twopizzas.di.Autowired;
import com.twopizzas.di.Component;
import com.twopizzas.domain.Booking;
import com.twopizzas.domain.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public interface BookingMapper extends DataMapper<Booking, EntityId, BookingSpecification> {
    @Override
    default Class<Booking> getEntityClass() {
        return Booking.class;
    }
}
