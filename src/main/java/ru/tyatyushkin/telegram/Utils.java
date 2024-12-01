package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static void init () {
        String init = "hello, max!";
        Bot.setInit(init);
    }

    public static String testParse(String getUpdates) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(getUpdates);
            JsonNode resultArray = jsonNode.get("result");
            System.out.println(resultArray.toPrettyString());
        } catch (Exception e) {
            LoggerConfig.logger.error("Ошибка: ", e);
        }
        return null;
    }

    public static void checkTelegramDir() {
        Path telegramDir = Paths.get("/opt/telegram");
        if (!Files.exists(telegramDir)) {
            try {
                Files.createDirectories(telegramDir);
                LoggerConfig.logger.info("/opt/telegram directory created.");
            } catch (IOException e) {
                LoggerConfig.logger.error("Failed to create /opt/telegram directory: ", e);
                System.exit(1);
            }
        } else {
            LoggerConfig.logger.info("/opt/telegram directory already exists.");
        }
    }


}
