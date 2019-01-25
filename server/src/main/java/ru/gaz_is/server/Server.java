package ru.gaz_is.server;

import org.apache.log4j.Logger;
import ru.gaz_is.client.Client;
import ru.gaz_is.common.CommandsVerificator;
import ru.gaz_is.common.DbTableCreator;
import ru.gaz_is.common.RequestsBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 4444;
    private static final Logger LOG = Logger.getLogger(Server.class);

    public static void main(String[] args) {
        try {
            new Server().serverRun();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverRun() throws IOException {
        LOG.info("Проверка существования таблицы БД");
        DbTableCreator.createTable();
        LOG.info("Подготовка БД успешно завершена!");

        ServerSocket server = new ServerSocket(PORT);
        LOG.info("Запуск клиента");
        new Thread(() -> {
            try {
                new Client().clientRun();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }).start();

        CommandsVerificator verificator = new CommandsVerificator();
        RequestsBuilder builder = new RequestsBuilder();
        LOG.info("Создание сокета для прослушивания соединения");
        while (!server.isClosed()) {

            try (Socket client = server.accept();
                 DataInputStream in = new DataInputStream(client.getInputStream());
                 DataOutputStream out = new DataOutputStream(client.getOutputStream())) {

                while (!client.isClosed()) {
                    LOG.info("Ожидание команды от клиента");
                    String command = in.readUTF();

                    LOG.info("Проверка корректности поступившей команды");
                    int commandId = verificator.verify(command, out);

                    if (commandId != -1) {
                        LOG.info("Команда корректна! Обработка поступившей команды");
                        String result = null;
                        switch (commandId) {
                            case 1:
                                result = builder.getAccount(command.split(" "));
                                break;
                            case 2:
                                result = builder.saveAccount(command.split(" "));
                                break;
                            case 3:
                                result = builder.updateAccount(command.split(" "));
                                break;
                            case 4:
                                result = builder.deleteAccount(command.split(" "));
                        }
                        LOG.info("Отправка ответа клиенту");
                        out.writeUTF(result);
                        out.flush();
                    }
                }
            } catch (EOFException e) {
                LOG.warn("Чтение входного потока было прервано! Остановка сервера...");
                server.close();
            }
        }
    }
}