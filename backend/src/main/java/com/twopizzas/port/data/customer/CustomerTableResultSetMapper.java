package com.twopizzas.port.data.customer;

import com.twopizzas.domain.Customer;
import com.twopizzas.domain.EntityId;
import com.twopizzas.port.data.DataMappingException;
import com.twopizzas.port.data.SqlResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerTableResultSetMapper implements SqlResultSetMapper<Customer> {
    public List<Customer> map(ResultSet resultSet) {
        List<Customer> mapped = new ArrayList<>();
        try {
            while (resultSet.next()) {
                mapped.add(new Customer(
                        EntityId.of(resultSet.getObject(CustomerMapperImpl.COLUMN_ID, String.class)),
                        resultSet.getObject(CustomerMapperImpl.COLUMN_CODE, String.class),
                        resultSet.getObject(CustomerMapperImpl.COLUMN_NAME, String.class),
                        resultSet.getObject(CustomerMapperImpl.COLUMN_LOCATION, String.class),
                        ZoneId.of(resultSet.getObject(CustomerMapperImpl.COLUMN_UTC_OFFSET, String.class))
                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Customer.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }
}
