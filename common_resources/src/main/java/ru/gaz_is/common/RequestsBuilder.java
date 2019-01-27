package ru.gaz_is.common;

import org.apache.log4j.Logger;
import ru.gaz_is.common.sql.ServerResponse;
import ru.gaz_is.common.util.DbTableCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestsBuilder {
    private static final Logger LOG = Logger.getLogger(RequestsBuilder.class);
    private RequestExecutor executor;

    public RequestsBuilder() {
        executor = new RequestExecutor();
    }

    public String getAccount(String[] command) {
        return executor.execute("SELECT ACCOUNT_NAME, SURNAME FROM ACCOUNTS WHERE ACCOUNT_NAME = ?", ps -> {
            LOG.info("Попытка получения данных аккаунта " + command[1] + " по имени");
            ps.setString(1, command[1]);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return "Аккаунт " + rs.getString("account_name") +
                        ", фамилия владельца - " + rs.getString("surname");
            } else {
                return ServerResponse.ACCOUNT_NOT_FOUND.getText();
            }
        });
    }

    public String updateAccount(String[] command) {
        return executor.execute("UPDATE ACCOUNTS SET SURNAME = ? WHERE ACCOUNTS.ACCOUNT_NAME = ?", ps -> {
            LOG.info("Попытка изменения данных аккаунта " + command[1]);
            ps.setString(1, command[2]);
            ps.setString(2, command[1]);
            int result = ps.executeUpdate();
            if (result != 0) {
                return ServerResponse.SURNAME_IS_CHANGED.getText();
            } else {
                return ServerResponse.ACCOUNT_NOT_FOUND.getText();
            }
        });
    }

    public String saveAccount(String[] command) {
        return executor.execute("INSERT INTO ACCOUNTS (account_name, surname) VALUES (?, ?)", ps -> {
            LOG.info("Попытка сохранения аккаунта " + command[1]);
            ps.setString(1, command[1]);
            ps.setString(2, command[2]);
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) {
                    return ServerResponse.ACCOUNT_ALREADY_EXISTS.getText();
                }
            }
            return ServerResponse.ACCOUNT_IS_SAVED.getText();
        });
    }

    public String deleteAccount(String[] command) {
        return executor.execute("DELETE FROM ACCOUNTS WHERE ACCOUNTS.ACCOUNT_NAME = ?", ps -> {
            LOG.info("Попытка удаления аккаунта " + command[1]);
            ps.setString(1, command[1]);
            int result = ps.executeUpdate();
            if (result != 0) {
                return ServerResponse.ACCOUNT_IS_DELETED.getText();
            } else {
                return ServerResponse.ACCOUNT_NOT_FOUND.getText();
            }
        });
    }
}