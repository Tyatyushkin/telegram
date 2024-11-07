package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean test_mode = Boolean.parseBoolean(System.getenv("TEST_MODE"));
        String app_token = System.getenv("TG_TOKEN");
        String x_token = System.getenv("X_TOKEN");
        String chatID = System.getenv("CHAT_ID");
        String x_username = System.getenv("X_USERNAME");
        int xTimer = 240;
        String userID;

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
