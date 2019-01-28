
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.gaz_is.client.Client;
import ru.gaz_is.common.sql.ConsoleInfo;
import ru.gaz_is.common.sql.ServerResponse;
import ru.gaz_is.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ApiTests {
    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Client client;

    @BeforeClass
    public static void createDataSources() throws IOException {
        new Thread(() -> {
            try {
                new Server().serverRun();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        client = mock(Client.class);
        socket = new Client().getClientSocket();
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @AfterClass
    public static void closeDataSources() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    private void runTest(String command, String expectedResult) throws IOException {
        when(client.readData(socket, in, out, command)).thenReturn(expectedResult);
        assertEquals(expectedResult, client.readData(socket, in, out, command));
        verify(client).readData(socket, in, out, command);
    }

    @Test
    public void unsuccessfulProgramExit() throws IOException {
        runTest("0", ConsoleInfo.PROGRAM_EXIT_VERIFY.getText());
        runTest("anyCommand", ServerResponse.WRONG_COMMAND.getText());
    }

    @Test
    public void callHelp() throws IOException {
        runTest("5", ConsoleInfo.HELP.getText());
    }

    @Test
    public void saveNewAccount() throws IOException {
        runTest("2 accountName Surname", ServerResponse.ACCOUNT_IS_SAVED.getText());
    }

    @Test
    public void saveExistingAccount() throws IOException {
        runTest("2 existAccountName Surname", ServerResponse.ACCOUNT_ALREADY_EXISTS.getText());
    }

    @Test
    public void getExistAccount() throws IOException {
        runTest("1 accountName", "Аккаунт accountName, фамилия владельца - Surname");
    }

    @Test
    public void getNotExistAccount() throws IOException {
        runTest("1 notExistAccountName", ServerResponse.ACCOUNT_NOT_FOUND.getText());
    }

    @Test
    public void changeExistAccount() throws IOException {
        runTest("3 accountName NewSurname", ServerResponse.SURNAME_IS_CHANGED.getText());
    }

    @Test
    public void changeNotExistAccount() throws IOException {
        runTest("3 notExistAccountName NewSurname", ServerResponse.ACCOUNT_NOT_FOUND.getText());
    }

    @Test
    public void deleteExistAccount() throws IOException {
        runTest("4 accountName", ServerResponse.ACCOUNT_IS_DELETED.getText());
    }

    @Test
    public void deleteNotExistAccount() throws IOException {
        runTest("4 notExistAccountName", ServerResponse.ACCOUNT_NOT_FOUND.getText());
    }
}