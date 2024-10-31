package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        String app_token = System.getenv("TG_TOKEN");

        Bot ma = new Bot(app_token);

        String updates = ma.getUpdates();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(updates);
        System.out.println(jsonNode.toPrettyString());

        while (true) {
            try {
                Thread.sleep(2000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
