package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean isCheck(String messages) {
        return messages != null;
    }

    private JsonNode getResult(String messages) throws JsonProcessingException {
        if (isCheck(messages)) {
            JsonNode rootNode = objectMapper.readTree(messages);
            return rootNode.get("result");
        }
        return null;
    }

    private boolean checkResult(JsonNode resultArray) {
        return resultArray != null && resultArray.isArray();
    }

    private void setTelegramUpdateId(JsonNode resultArray) {
        for (JsonNode update : resultArray) {
            int updateId = update.get("update_id").asInt();
            JsonNode messageNode = update.get("message");
            Telegram.setLastUpdateId(updateId);
        }
    }
}
