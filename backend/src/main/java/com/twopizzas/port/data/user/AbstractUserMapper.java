package com.twopizzas.port.data.user;

import com.twopizzas.di.Autowired;
import com.twopizzas.domain.EntityId;
import com.twopizzas.domain.user.User;
import com.twopizzas.port.data.SqlStatement;
import com.twopizzas.port.data.db.ConnectionPool;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;


public abstract class AbstractUserMapper<T extends User> {
    public static final String TABLE_USER = "\"user\"";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TYPE = "userType";
    public static final String COLUMN_STATUS = "status";

    private static final String CREATE_TEMPLATE =
            "INSERT INTO " + TABLE_USER +
                    " (" + COLUMN_ID + ", " + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_TYPE + ", " + COLUMN_STATUS + ")" +
                    " VALUES (?, ?, crypt(?, gen_salt('bf')), ?, ?);";

    private static final String UPDATE_TEMPLATE =
  // password has not changed do not encrypt again
            "UPDATE " + TABLE_USER +
            " SET " + COLUMN_USERNAME + " = ?, " +
                    COLUMN_PASSWORD + " = CASE WHEN password = ? THEN ? ELSE crypt(?, gen_salt('bf')) END, " +
                    COLUMN_TYPE + " = ?, " +
                    COLUMN_STATUS + " = ?" +
            " WHERE id = ?;";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_USER + " WHERE id = ?;";

    private static final String SELECT =
            "SELECT *" +
                    " FROM \"user\"" +
                    " LEFT JOIN customer ON \"user\".id = customer.id" +
                    " LEFT JOIN administrator ON \"user\".id = administrator.id" +
                    " LEFT JOIN airline ON \"user\".id = airline.id" +
                    " WHERE \"user\".id = ?;";

    private final ConnectionPool connectionPool;

    @Autowired
    public AbstractUserMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected void abstractCreate(T entity) {
        new SqlStatement(CREATE_TEMPLATE,
                entity.getId().toString(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserType(),
                entity.getStatus().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    protected void abstractUpdate(T entity) {
        new SqlStatement(UPDATE_TEMPLATE,
                entity.getUsername(),
                entity.getPassword(),
                entity.getPassword(),
                entity.getPassword(),
                entity.getUserType(),
                entity.getStatus().toString(),
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    protected void abstractDelete(T entity) {
        new SqlStatement(DELETE_TEMPLATE,
                entity.getId().toString()
        ).doExecute(connectionPool.getCurrentTransaction());
    }

    protected ResultSet abstractRead(EntityId id) {
        return new SqlStatement(SELECT, id.toString()).doQuery(connectionPool.getCurrentTransaction());
    }
}
