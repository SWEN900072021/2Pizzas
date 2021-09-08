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

                // TODO: get username and password from user table using id
                String username = null;
                String password = null;

//                mapped.add(new Customer(
//                        EntityId.of(resultSet.getObject(CustomerMapperImpl.COLUMN_ID, String.class)),
//                        username,
//                        password,
//                        resultSet.getObject(CustomerMapperImpl.COLUMN_GIVENNAME, String.class),
//                        resultSet.getObject(CustomerMapperImpl.COLUMN_SURNAME, String.class),
//                        resultSet.getObject(CustomerMapperImpl.COLUMN_EMAIL, String.class)
//                ));
            }
        } catch (SQLException e) {
            throw new DataMappingException(String.format(
                    "failed to map results from result set to %s entity, error: %s", Customer.class.getName(), e.getMessage()),
                    e);
        }
        return mapped;
    }

    @Override
    public Customer mapOne(ResultSet resultSet) {
        return null;
    }
}
