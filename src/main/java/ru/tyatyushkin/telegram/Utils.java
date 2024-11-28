package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
