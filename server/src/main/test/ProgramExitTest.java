import org.junit.Before;
import org.junit.Test;
import ru.gaz_is.client.Client;
import ru.gaz_is.common.sql.ConsoleInfo;
import ru.gaz_is.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProgramExitTest {
    private Client client;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @Test
    public void programExit() throws IOException {
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

        runTest("0", ConsoleInfo.PROGRAM_EXIT_VERIFY.getText());
        runTest("y", ConsoleInfo.PROGRAM_EXIT.getText());
    }

    private void runTest(String command, String expectedResult) throws IOException {
        when(client.readData(socket, in, out, command)).thenReturn(expectedResult);
        assertEquals(expectedResult, client.readData(socket, in, out, command));
        verify(client).readData(socket, in, out, command);
    }
}