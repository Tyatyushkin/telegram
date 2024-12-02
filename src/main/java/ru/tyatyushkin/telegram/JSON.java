package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;



public class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static boolean isCheck(String messages) {
        return messages != null;
    }

    private static JsonNode getResult(String messages) throws JsonProcessingException {
        if (isCheck(messages)) {
            JsonNode rootNode = objectMapper.readTree(messages);
            return rootNode.get("result");
        }
        return null;
    }

    private static ArrayNode parseMessages(JsonNode resultArray) {
        ArrayNode test = objectMapper.createArrayNode();
        for (JsonNode update : resultArray) {
            test.add(update.get("message"));
        }
        return test;
    }

    private static void testAnswer(ArrayNode test) {
        for (JsonNode jsonNode : test) {
            if (jsonNode.has("text")) {
                String text = jsonNode.get("text").asText();
                String chatId = jsonNode.get("chat").get("id").asText();
                if (text.equalsIgnoreCase("hello")) {
                    Telegram.getSendMessage(chatId, "how are you?");
                }
            }
        }
    }

    private static void testReplyMessage(JsonNode message) {
        for (JsonNode jsonNode : message) {
            if (jsonNode.has("channel_post")) {
                JsonNode channelPost = jsonNode.get("channel_post");
                System.out.println(channelPost.toPrettyString());
            }
        }
    }

    private static boolean checkResult(JsonNode resultArray) {
        return resultArray != null && resultArray.isArray();
    }

    private static void setTelegramUpdateId(JsonNode resultArray) {
        for (JsonNode update : resultArray) {
            int updateId = update.get("update_id").asInt();
            Telegram.setLastUpdateId(updateId);
        }
    }

    public static void testTelegramParse(String getUpdates) throws JsonProcessingException {
        JsonNode result = getResult(getUpdates);
        if (checkResult(result)) {
            testAnswer(parseMessages(result));
            testReplyMessage(result);
            setTelegramUpdateId(result);
        }
    }
}
