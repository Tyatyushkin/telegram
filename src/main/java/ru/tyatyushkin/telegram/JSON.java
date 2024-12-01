package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean isCheck(String messages) {
        return messages != null;
    }

    private String getResult(String messages) throws JsonProcessingException {
        if (isCheck(messages)) {
            JsonNode rootNode = objectMapper.readTree(messages);
            JsonNode resultArray = rootNode.get("result");
            return resultArray.toString();
        }
        return null;
    }
}
