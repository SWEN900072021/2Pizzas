package com.twopizzas.port.data.user;

import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.util.List;

public class UserByUsernameAndPasswordSpecification implements UserSpecification {

    private static final String TEMPLATE =
            "SELECT *" +
            " FROM \"user\"" +
            " LEFT JOIN customer ON \"user\".id = customer.id" +
            " LEFT JOIN administrator ON \"user\".id = administrator.id" +
            " LEFT JOIN airline ON \"user\".id = airline.id" +
            " WHERE \"user\".username = ? AND \"user\".password is NOT NULL AND \"user\".password = crypt(?, \"user\".password);";

    private final UserMapper mapper;
    private final String username;
    private final String password;

    public UserByUsernameAndPasswordSpecification(UserMapper mapper, String username, String password) {
        this.mapper = mapper;
        this.username = username;
        this.password = password;
    }

    @Override
    public List<User> execute(ConnectionPool context) {
        return mapper.map(new SqlStatement(TEMPLATE, username, password).doQuery(context.getCurrentTransaction()));
    }
}
