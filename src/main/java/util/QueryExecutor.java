package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private final Connection connection;

    public QueryExecutor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String query) throws SQLException {
        try(Statement stmt = connection.createStatement()){
            stmt.execute(query);
        }
    }

    public <T> T execQuery(String query, QueryResultHandler<T> resultHandler) throws SQLException {
        T value = null;
        try(Statement stmt = connection.createStatement()){
            stmt.executeQuery(query);

            try(ResultSet resultSet = stmt.getResultSet()){
                value = resultHandler.handle(resultSet);
            }
        }
        return value;
    }

    public Connection getConnection() {
        return connection;
    }
}
