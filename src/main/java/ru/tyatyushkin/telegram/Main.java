package ru.tyatyushkin.telegram;



public class Main {
    public static void main(String[] args)  {
        LoggerConfig.initialize();

        boolean test_mode = Boolean.parseBoolean(System.getenv("TEST_MODE"));
        String app_token = System.getenv("TG_TOKEN");

        if (app_token == null) {
            LoggerConfig.logger.error("Ошибка: Переменная окружения TG_TOKEN не задана!");
            System.exit(1);
        }

        if (test_mode) {
            Bot test = new Bot(app_token);
            test.createTestBot();
        } else {
           Bot telegram = new Bot(app_token);
           telegram.createBot();
        }
    }
}
