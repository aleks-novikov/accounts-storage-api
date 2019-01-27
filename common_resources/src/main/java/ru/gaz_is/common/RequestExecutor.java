package ru.gaz_is.common;

import ru.gaz_is.common.sql.ServerResponse;
import ru.gaz_is.common.util.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RequestExecutor {
    private final ConnectionFactory connectionFactory;

    RequestExecutor() {
        connectionFactory = () -> DriverManager.getConnection(Config.get().getUrl());
    }

    String execute(String statement, SqlExecutor settings) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {
            return settings.execute(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}