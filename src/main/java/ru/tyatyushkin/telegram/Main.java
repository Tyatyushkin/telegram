package ru.tyatyushkin.telegram;


public class Main {
    public static void main(String[] args) throws Exception {
        boolean test_mode = Boolean.parseBoolean(System.getenv("TEST_MODE"));
        String app_token = System.getenv("TG_TOKEN");

        if (app_token == null) {
            System.out.println("Переменная окружения TG_TOKEN не задана!");
            return;
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
