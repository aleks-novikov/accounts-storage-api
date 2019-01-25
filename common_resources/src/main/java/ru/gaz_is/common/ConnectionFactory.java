package ru.gaz_is.common;

import java.sql.Connection;
import java.sql.SQLException;

interface ConnectionFactory {
    Connection getConnection() throws SQLException;
}