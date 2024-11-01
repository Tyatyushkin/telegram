package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        String app_token = System.getenv("TG_TOKEN");

        if (app_token == null) {
            System.out.println("Переменная окружения TG_TOKEN не задана!");
            return;
        }

        Bot ma = new Bot(app_token);

        while (true) {
            String updates = ma.getUpdates();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(updates);
            JsonNode resultArray = jsonNode.get("result");
            System.out.println(jsonNode.toPrettyString());

            for (JsonNode update : resultArray) {
                int updateId = update.get("update_id").asInt();
                JsonNode messageNode = update.get("message");
                if (messageNode != null && messageNode.get("text") != null) {
                    String text = messageNode.get("text").asText();
                    String chatId = messageNode.get("chat").get("id").asText();

                    if (text.toLowerCase().startsWith("gpt")) {
                        ma.sendMessage(chatId, "Адвокат еще написал методы взаимодействия с chatGPT, но он очень старается!");
                    }
                    if (text.toLowerCase().contains("сиськи")) {
                        ma.sendMessage(chatId, "Мастерплан еще не придумал как сюда загружать фото сисек, но он о них постоянно думает!");
                    }
                    if (text.toLowerCase().contains("python")) {
                        ma.sendMessage(chatId, "Кому что? А ебуняке лишь бы питона душить");
                    }
                }
                Bot.setLastUpdateId(updateId);

            }
            try {
                Thread.sleep(2000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
