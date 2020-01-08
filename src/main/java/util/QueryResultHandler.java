package util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @param <T>
 */
@FunctionalInterface
public interface QueryResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
