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

    public void execUpdate(String query) {
        try(Statement stmt = connection.createStatement()){
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> T execQuery(String query, QueryResultHandler<T> resultHandler)  {
        T value = null;
        try(Statement stmt = connection.createStatement()){
            stmt.executeQuery(query);

            try(ResultSet resultSet = stmt.getResultSet()){
                value = resultHandler.handle(resultSet);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public Connection getConnection() {
        return connection;
    }
}
