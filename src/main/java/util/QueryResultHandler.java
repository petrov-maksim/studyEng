package util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Функциональный интерфейс, имплементации которого (лямбда выражения), будут обрабатывать ResultSet,
 * возвращаемый после исполнения Statement
 */
@FunctionalInterface
public interface QueryResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
