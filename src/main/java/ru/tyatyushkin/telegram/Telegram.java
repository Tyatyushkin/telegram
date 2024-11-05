package ru.tyatyushkin.telegram;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class Telegram {

    //TODO перенести методы и переменные из Bot.java в этот класс

    public void sendMessage(String chatId, String message) {
        try {
            // Create JSON
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("chat_id", chatId);
            jsonObject.put("text", message);

            // Show JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject.toString());
            System.out.println("---===SHOW JSON==--");
            System.out.println(rootNode.toPrettyString());
            System.out.println("--==END==--");

            // Создаём URL для метода sendMessage
            URL url = new URL("https://api.telegram.org/bot");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Настраиваем параметры запроса
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            // Отправляем JSON
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Проверяем код ответа сервера
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Message sent successfully.");
            } else {
                System.out.println("Failed to send message. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
