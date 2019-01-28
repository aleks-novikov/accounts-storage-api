
import org.junit.BeforeClass;
import org.junit.Test;
import ru.gaz_is.client.Client;
import ru.gaz_is.common.ConnectionFactory;
import ru.gaz_is.common.sql.ConsoleInfo;
import ru.gaz_is.common.sql.ServerResponse;
import ru.gaz_is.common.util.Config;
import ru.gaz_is.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

interface SqlExecutor {
    void execute(PreparedStatement ps) throws SQLException;
}

public class ApiTests {
    private static Socket client;
    private static DataInputStream in;
    private static DataOutputStream out;
    private String command;
    private String expectedResult;

    @BeforeClass
    public static void createDataSources() throws IOException {
        new Thread(() -> {
            try {
                new Server().serverRun();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        client = new Client().getClientSocket();
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
    }

    private void addData() {
        execute("INSERT INTO accounts VALUES ('accountName','Surname')", PreparedStatement::executeUpdate);
    }

    private void deleteData() {
        execute("DELETE FROM accounts WHERE account_name = 'accountName'", PreparedStatement::executeUpdate);
    }

    private void execute(String statement, SqlExecutor executor) {
        ConnectionFactory cf = () -> DriverManager.getConnection(Config.get().getUrl());
        try (Connection connection = cf.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {
            executor.execute(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unsuccessfulProgramExit() throws IOException {
        expectedResult = ConsoleInfo.PROGRAM_EXIT_VERIFY.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, "0"));

        expectedResult = ServerResponse.WRONG_COMMAND.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, "anyCommand"));
    }

    @Test
    public void callHelp() throws IOException {
        expectedResult = ConsoleInfo.HELP.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, "5"));
    }

    @Test
    public void saveNewAccount() throws IOException {
        command = "2 accountName Surname";
        expectedResult = ServerResponse.ACCOUNT_IS_SAVED.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));
        deleteData();
    }

    @Test
    public void saveExistingAccount() throws IOException {
        addData();
        command = "2 accountName Surname";
        expectedResult = ServerResponse.ACCOUNT_ALREADY_EXISTS.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));
        deleteData();
    }

    @Test
    public void getExistAccount() throws IOException {
        addData();
        command = "1 accountName";
        expectedResult = "Аккаунт accountName, фамилия владельца - Surname";
        assertEquals(expectedResult, Client.readData(client, in, out, command));
        deleteData();
    }

    @Test
    public void getNotExistAccount() throws IOException {
        command = "1 notExistAccountName";
        expectedResult = ServerResponse.ACCOUNT_NOT_FOUND.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));
    }

    @Test
    public void changeExistAccount() throws IOException {
        addData();
        command = "3 accountName NewSurname";
        expectedResult = ServerResponse.SURNAME_IS_CHANGED.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));

        command = "1 accountName";
        expectedResult = "Аккаунт accountName, фамилия владельца - NewSurname";
        assertEquals(expectedResult, Client.readData(client, in, out, command));
        deleteData();
    }

    @Test
    public void changeNotExistAccount() throws IOException {
        command = "3 notExistAccountName NewSurname";
        expectedResult = ServerResponse.ACCOUNT_NOT_FOUND.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));
    }

    @Test
    public void deleteExistAccount() throws IOException {
        addData();
        command = "4 accountName";
        expectedResult = ServerResponse.ACCOUNT_IS_DELETED.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));

        command = "1 accountName";
        expectedResult = ServerResponse.ACCOUNT_NOT_FOUND.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));
    }

    @Test
    public void deleteNotExistAccount() throws IOException {
        command = "4 notExistAccountName";
        expectedResult = ServerResponse.ACCOUNT_NOT_FOUND.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, command));
    }
}