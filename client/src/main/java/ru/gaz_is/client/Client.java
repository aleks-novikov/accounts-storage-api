package ru.gaz_is.client;

import org.apache.log4j.Logger;
import ru.gaz_is.common.sql.ConsoleInfo;
import ru.gaz_is.common.util.Config;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class);
    private static final String IP_ADDRESS = Config.get().getServerIp();
    private static final int SERVER_PORT = Config.get().getSocketPort();
    private static Socket client;

    public Socket getClientSocket() {
        return client;
    }

    static {
        try {
            LOG.info("Инициализация сокета клиента");
            client = new Socket(InetAddress.getByName(IP_ADDRESS), SERVER_PORT);
        } catch (IOException e) {
            LOG.error("Ошибка запуска клиента!");
            e.printStackTrace();
        }
    }

    public void clientRun() throws IOException {
        try (DataInputStream in = new DataInputStream(client.getInputStream());
             DataOutputStream out = new DataOutputStream(client.getOutputStream())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            ConsoleInfo.HELP.print();
            while (!client.isClosed()) {
                LOG.info("Ожидание ввода команды с консоли");
                ConsoleInfo.NOTE.print();
                String command = reader.readLine();
                readData(client, in, out, command);
            }
        }
    }

    public static String readData(Socket client, DataInputStream in, DataOutputStream out, String accountName) throws IOException {
        LOG.info("Отправка команды серверу");
        out.writeUTF(accountName);
        out.flush();

        LOG.info("Ожидание ответа от сервера");
        String response = in.readUTF();
        LOG.info("Ответ получен. Вывод ответа на консоль");
        System.out.println(response);

        if (response.contains("Остановка программы")) {
            client.close();
        }
        return response;
    }
}