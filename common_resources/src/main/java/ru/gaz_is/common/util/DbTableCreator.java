package ru.gaz_is.common.util;

import ru.gaz_is.common.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTableCreator {
    private ConnectionFactory connectionFactory;

    public DbTableCreator() {
        connectionFactory = () -> DriverManager.getConnection(Config.get().getUrl());
    }

    public void createTable() {
        try (Connection connection = connectionFactory.getConnection()) {
            String createTable = "CREATE TABLE IF NOT EXISTS accounts(" +
                    "account_name varchar(30) UNIQUE NOT NULL," +
                    "surname VARCHAR(30) NOT NULL);";
            connection.createStatement().executeUpdate(createTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка при попытке создать таблицу базы данных! " + e.getMessage());
        }
    }
}