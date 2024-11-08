package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import java.util.concurrent.TimeUnit;


public class Bot {
    private final String token;
    private String chatID;

    public Bot(String token) {
        this.token = token;
    }
    //TODO добавить инициализацию всех переменных

    public void initialize() {
        System.out.println("--==START INITIALIZE==--");
        String app_token = System.getenv("TG_TOKEN");
        String chatId = System.getenv("CHAT_ID");
        if (app_token == null) {
            System.out.println("Ошибка: Задайте значение переменной TG_TOKEN");
            System.exit(1);
        } else {
            System.out.println("TG_TOKEN - прочитан");
        }
        if (chatId == null) {
            System.out.println("Ошибка: Задайте значение пременной CHAT_ID");
            System.exit(1);
        } else {
            System.out.println("CHAT_ID - прочтан");
            this.chatID = chatId;
        }
        System.out.println("--==END INITIALIZE==--");
    }

    public void createBot() {
        initialize();
    }

    public void createTestBot() {
        // Инициализация и проверка переменных
        initialize();
        // Подключаем бота
        Telegram telegram = new Telegram(token);
        // Создаем новый планировщик
        Scheduler scheduler = new Scheduler();
        // Тестирование расписания
        Runnable morning = () -> {
            System.out.println("--==TEST SCHEDULER==--");
            telegram.sendMessage(chatID,"**ТЕСТ Сообщения по расписанию** ~~В 11:00~~");
            telegram.sendMessage(chatID,"Привет жалкие ||людишки||");
            System.out.println("--==END==--");
        };

        Runnable getUpdates = () -> {
            try {
                String updates = telegram.getUpdates();
                if (updates != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(updates);
                    JsonNode resultArray = jsonNode.get("result");
                    //System.out.println(resultArray.toPrettyString());
                    for (JsonNode update : resultArray) {
                        int updateId = update.get("update_id").asInt();
                        JsonNode messageNode = update.get("message");
                        if (messageNode != null && messageNode.get("text") != null) {
                            String text = messageNode.get("text").asText();
                            String chatId = messageNode.get("chat").get("id").asText();

                            if (text.toLowerCase().startsWith("хуй")) {
                                System.out.println("хуй");
                                telegram.sendMessage(chatId, "Трусы свои пожуй\\!");
                            }
                            if (text.toLowerCase().contains("ебуняка")) {
                                System.out.println("ебуняка");
                                telegram.sendMessage(chatId, "опять эта хитрая рожа что\\-то задумала");
                            }
                            if (text.toLowerCase().contains("нюдсы")) {
                                System.out.println("нюдсы");
                                telegram.sendPhoto(chatID, "https://pbs.twimg.com/media/GVjuLO-WwAAzjU_?format=jpg&name=large");
                            }
                        }
                        Telegram.setLastUpdateId(updateId);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        };

        scheduler.addTaskAtFixedRate(getUpdates, 0, 5, TimeUnit.SECONDS);
        scheduler.addTaskDaily(morning, 7, 0);
    }
}
