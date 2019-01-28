package ru.gaz_is.common.sql;

public enum ConsoleInfo {
    NOTE("Пожалуйста, введите номер команды и входные данные:"),
    HELP("Примеры команд:\n" +
            "0 - выход из программы\n" +
            "1 - найти аккаунт по имени: 1 имя_аккаунта\n" +
            "2 - сохранить новый аккаунт: 2 имя_аккаунта фамилия\n" +
            "3 - изменить фамилию аккаунта: 3 имя_аккаунта новая_фамилия\n" +
            "4 - удалить аккаунт: 4 имя_аккаунта\n" +
            "5 - вызвать справку\n"),
    PROGRAM_EXIT_VERIFY("Вы уверены, что хотите выйти из программы? Для подтверждения нажмите Y"),
    PROGRAM_EXIT("Остановка программы...");

    private String text;

    ConsoleInfo(String text) {
        this.text = text;
    }

    public void print() {
        System.out.println(text);
    }

    public String getText() {
        return text;
    }
}