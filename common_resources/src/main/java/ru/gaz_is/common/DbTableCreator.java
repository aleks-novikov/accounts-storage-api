package ru.gaz_is.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTableCreator {
    private final static String URL = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1";

    public static String getURL() {
        return URL;
    }

    public static void createTable() {
        final ConnectionFactory connectionFactory = () -> DriverManager.getConnection(URL);

        try (Connection connection = connectionFactory.getConnection()) {
            String createTable = "CREATE TABLE IF NOT EXISTS accounts(" +
                    "id int AUTO_INCREMENT," +
                    "account_name varchar(30) UNIQUE NOT NULL," +
                    "surname VARCHAR(30) NOT NULL);";
            connection.createStatement().executeUpdate(createTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка при попытке создать таблицу базы данных! " + e.getMessage() + e.getCause());
        }
    }
}