package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        String app_token = System.getenv("TG_TOKEN");
        String x_token = System.getenv("X_TOKEN");
        String chatID = System.getenv("CHAT_ID");
        String x_username = System.getenv("X_USERNAME");
        int xTimer = 240;
        String userID;
        ;
        if (app_token == null) {
            System.out.println("Переменная окружения TG_TOKEN не задана!");
            return;
        }

        Bot ma = new Bot(app_token);
        X twitter = new X(x_token);
        userID = twitter.getUserIdByUsername(x_username);
        int xCount = 0;
        String lastTweet = null;
        String  newTweet;

        while (true) {
            //add X methods
            if (xCount >= xTimer) {
                newTweet = twitter.getLastTweetByUserId(userID);
                xCount = 0;
                if (!newTweet.equals(lastTweet)) {
                    ma.sendMessage(chatID, "Какие есть новости от Мишеньки?:  " + twitter.getMessageByMessageId(newTweet).replaceAll("\\n", " "));
                    lastTweet = newTweet;
                }
            } else {
                xCount++;
            }

            String updates = ma.getUpdates();
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
                            ma.sendMessage(chatId, "Адвокат еще написал методы взаимодействия с chatGPT, но он очень старается!");
                        }
                        if (text.toLowerCase().contains("сиськи")) {
                            ma.sendMessage(chatId, "Мастерплан еще не придумал как сюда загружать фото сисек, но он о них постоянно думает!");
                        }
                        if (text.toLowerCase().contains("python")) {
                            ma.sendMessage(chatId, "Кому что? А ебуняке лишь бы питона душить");
                        }
                        if (text.toLowerCase().startsWith("red")) {
                            ma.sendPhoto(chatId, "https://i.pinimg.com/736x/f7/1f/05/f71f05c7f854aaaeb40800bba1709f2e.jpg");
                        }
                    }
                    Bot.setLastUpdateId(updateId);
                }
            }
            try {
                Thread.sleep(5000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
