package ru.gaz_is.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecutor {
    String execute(PreparedStatement ps) throws SQLException;
}
