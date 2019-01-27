package ru.gaz_is.common.sql;

public enum ServerResponse {
    ACCOUNT_IS_SAVED("Введённые данные аккаунта успешно сохранены!"),
    ACCOUNT_IS_DELETED("Аккаунт успешно удалён!"),
    ACCOUNT_ALREADY_EXISTS("В базе данных уже существует аккаунт с указанным именем!"),
    ACCOUNT_NOT_FOUND("По введённому вами имени не найдено ни одного аккаунта! Проверьте данные и попробуйте снова."),
    SURNAME_IS_CHANGED("Фамилия аккаунта успешно изменена!"),
    WRONG_COMMAND("Вы ввели неверную команду. Для получения справки введите 5."),
    LACK_OF_DATA("Вы указали не все данные, необходимые для поиска аккаунта. Для получения справки введите 5.");

    String text;

    ServerResponse(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}