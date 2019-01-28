package ru.gaz_is.common;

import ru.gaz_is.common.sql.ConsoleInfo;
import ru.gaz_is.common.sql.ServerResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public class CommandsVerificator {
    private String[] line;

    public int verify(String command, DataOutputStream out) throws IOException {
        if (command.toLowerCase().equals("y")) {
            out.writeUTF(ConsoleInfo.PROGRAM_EXIT.getText());
            out.flush();
            return -1;
        }

        int commandId;
        try {
            line = command.split(" ");
            commandId = Integer.valueOf(line[0]);
        } catch (NumberFormatException e) {
            commandId = -1;
        }
        switch (commandId) {
            case 1:
            case 2:
            case 3:
            case 4:
                //проверка, все ли данные были введены
                if ((line.length == 1) ||
                        (((commandId == 2) || commandId == 3) && line.length < 3)) {
                    out.writeUTF(ServerResponse.LACK_OF_DATA.getText());
                    break;
                }
                return commandId;
            case 5:
                out.writeUTF(ConsoleInfo.HELP.getText());
                break;
            case 0:
                out.writeUTF(ConsoleInfo.PROGRAM_EXIT_VERIFY.getText());
                break;
            default:
                out.writeUTF(ServerResponse.WRONG_COMMAND.getText());
        }
        out.flush();
        return -1;
    }
}