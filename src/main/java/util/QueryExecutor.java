package util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс имеющий непосредственный доступ к Connection и осуществляющий обращения к БД
 */
public class QueryExecutor {
    private final Connection connection;
    public QueryExecutor(Connection connection) {
        this.connection = connection;
    }

    /**
     * Используется для DML операторов: DELETE, INSERT, UPDATE
     * @param query - sql
     */
    public void execUpdate(String query){
        try(Statement stmt = connection.createStatement()){
            stmt.execute(query);
        }catch (SQLException e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Can't update DB, query: \n\t" + query, e);
        }
    }

    /**
     * Используется для DQL операторов: SELECT
     * @param query - sql
     */
    public <T> T execQuery(String query, QueryResultHandler<T> resultHandler) {
        T value = null;
        try(Statement stmt = connection.createStatement()){
            stmt.executeQuery(query);

            try(ResultSet resultSet = stmt.getResultSet()){
                value = resultHandler.handle(resultSet);
            }
        }catch (SQLException e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Can't execute query: \n\t" + query, e);
        }
        return value;
    }

    /**
     * Метод получения PreparedStatement для sql
     */
    public PreparedStatement prepareStatement(String sql){
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * Метод исполнения batch-запросов
     */
    public void executeBatch(PreparedStatement stmt){
        try {
            stmt.executeBatch();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
    }
}
