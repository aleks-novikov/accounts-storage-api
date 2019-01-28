import org.junit.BeforeClass;
import org.junit.Test;
import ru.gaz_is.client.Client;
import ru.gaz_is.common.sql.ConsoleInfo;
import ru.gaz_is.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class ProgramExitTest {
    @Test
    public void programExit() throws IOException {
        Socket client = new Client().getClientSocket();
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        String expectedResult = ConsoleInfo.PROGRAM_EXIT_VERIFY.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, "0"));

        expectedResult = ConsoleInfo.PROGRAM_EXIT.getText();
        assertEquals(expectedResult, Client.readData(client, in, out, "y"));
    }
}