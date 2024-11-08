package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


public class Bot {
    private final String token;
    private String chatID;

    public Bot(String token) {
        this.token = token;
    }

    public void initialize() {
        System.out.println("--==START INITIALIZE==--");
        String app_token = System.getenv("TG_TOKEN");
        String chatId = System.getenv("CHAT_ID");
        String x_token = System.getenv("X_TOKEN");
        String x_username = System.getenv("X_USERNAME");
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
        if (x_token == null) {
            System.out.println("Ошибка: Задайте значение пременной X_TOKEN");
            System.exit(1);
        }
        else {
            System.out.println("X_TOKEN - прочитан");
        }
        if (x_username == null) {
            System.out.println("Ошибка: Задайте значение пременной X_USERNAME");
            System.exit(1);
        } else {
            System.out.println("X_USERNAME - прочитан");
        }
        System.out.println("--==END INITIALIZE==--");
    }

    public void createBot() {
        initialize();
        Telegram telegram = new Telegram(token);
        Scheduler scheduler = new Scheduler();

        Runnable getUpdates = () -> {
            try {
                String updates = telegram.getUpdates();
                if (updates != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(updates);
                    JsonNode resultArray = jsonNode.get("result");
                    for (JsonNode update : resultArray) {
                        int updateId = update.get("update_id").asInt();
                        JsonNode messageNode = update.get("message");
                        if (messageNode != null && messageNode.get("text") != null) {
                            String text = messageNode.get("text").asText();
                            String chatId = messageNode.get("chat").get("id").asText();

                            if (text.toLowerCase().startsWith("gpt")) {
                                telegram.sendMessage(chatId, "Адвокат когда ты уже сделаешь меня умным\\?");
                            }
                            if (text.toLowerCase().startsWith("хуй")) {
                                telegram.sendMessage(chatId, "трусы свои пожуй\\!");
                            }
                            if (text.toLowerCase().contains("python")) {
                                telegram.sendMessage(chatId, "Кому, что а ебуняке лишь бы питона душить");
                            }
                            if (text.toLowerCase().startsWith("red")) {
                                telegram.sendMessage(chatId, "Рыжие ушли на покой, попробуй написать нюдсы");
                            }
                            if (text.toLowerCase().startsWith("нюдсы")) {
                                telegram.sendPhoto(chatId, "https://pbs.twimg.com/media/GVjuLO-WwAAzjU_?format=jpg&name=large");
                            }
                        }
                        telegram.setLastUpdateId(updateId);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        };
        //TODO добавить интеграцию с X
        scheduler.addTaskDaily(() -> telegram.sendMessage(chatID,"Утро, мешки с костями\\!"), 7, 0);
        scheduler.addTaskAtFixedRate(getUpdates, 0, 5, TimeUnit.SECONDS);
    }

    public void createTestBot() {
        // Инициализация и проверка переменных
        initialize();
        // Подключаем бота
        Telegram telegram = new Telegram(token);
        // Создаем новый планировщик
        Scheduler scheduler = new Scheduler();
        scheduler.addTaskAtFixedRate(() -> {
            try {
                processUpdates(telegram.getUpdates(), telegram);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }, 0, 5, TimeUnit.SECONDS);
    }

    public void processUpdates(String getUpdates, Telegram telegram) throws JsonProcessingException {
        //Попробовать переписать на JSON
        if (getUpdates != null) {
            JSONObject json = new JSONObject(getUpdates);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(getUpdates);
            JsonNode resultArray = jsonNode.get("result");
            System.out.println(jsonNode.toPrettyString());

            for (JsonNode update : resultArray) {
                int updateId = update.get("update_id").asInt();
                JsonNode messageNode = update.get("message");
                if (messageNode != null && messageNode.get("text") != null) {
                    String text = messageNode.get("text").asText();
                    String chatId = messageNode.get("chat").get("id").asText();

                    if (text.toLowerCase().contains("тест")) {
                        telegram.sendMessage(chatId, "Что мудила криворукая ничего с первого раза сделать не можешь\\?");
                    }
                    if (text.toLowerCase().contains("нюдсы")) {
                        telegram.sendPhoto(chatId, "https://pbs.twimg.com/media/GVjuLO-WwAAzjU_?format=jpg&name=large");
                    }
                }
                telegram.setLastUpdateId(updateId);
            }
        }
    }
}
