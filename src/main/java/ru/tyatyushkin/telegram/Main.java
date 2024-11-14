package ru.tyatyushkin.telegram;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args)  {
        boolean test_mode = Boolean.parseBoolean(System.getenv("TEST_MODE"));
        String app_token = System.getenv("TG_TOKEN");

        if (app_token == null) {
            logger.error("Ошибка: Переменная окружения TG_TOKEN не задана!");
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
