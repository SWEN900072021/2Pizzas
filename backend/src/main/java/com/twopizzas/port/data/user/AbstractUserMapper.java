package com.twopizzas.port.data.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.User;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.sql.ResultSet;


public abstract class AbstractUserMapper<T extends User> {
    public static final String TABLE_USER = "\"user\"";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TYPE = "userType";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_USER + "(" + COLUMN_ID + ", " + COLUMN_USERNAME +
                    ", " + COLUMN_PASSWORD + ", " + COLUMN_TYPE + ")" + " VALUES (?, ?, ?, ?);";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_USER +
                    " SET " + COLUMN_USERNAME + " = ?, " + COLUMN_PASSWORD + " = ?, " + COLUMN_TYPE + " = ?" +
                    " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_USER + " WHERE id = ?;";

    private static final String SELECT =
            "SELECT * FROM pizzaUser JOIN customer JOIN admin JOIN airline ON pizzaUser.id = customer.id OR pizzaUser.id = admin.id OR pizzaUser.id = airline.id WHERE id = ?;";

    private ConnectionPool connectionPool;

    @Autowired
    public AbstractUserMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void abstractCreate(T entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserType()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    public void abstractUpdate(T entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserType(),
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    public void abstractDelete(T entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    protected ResultSet abstractRead(EntityId id) {
        return new SqlStatement(SELECT, id.toString()).doQuery(connectionPool.getCurrentTransaction());
    }
}
