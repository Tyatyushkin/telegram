package ru.tyatyushkin.telegram;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class Telegram {
    private static final String API_URL = "https://api.telegram.org/bot";
    private final String token;
    private static int lastUpdateId = 0;

    public Telegram(String token) {
        this.token = token;
    }

    public static void setLastUpdateId(int updateId) {
        lastUpdateId = updateId;
    }

    public String getUpdates() {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(API_URL + token + "/getUpdates?offset=" + (lastUpdateId + 1));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                conn.disconnect();
                return content.toString();
            } else {
                System.out.println("Error: " + responseCode + " - " + conn.getResponseMessage());
                conn.disconnect();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return null;
    }

    public void sendMessage(String chatId, String message) {
        try {
            // Create JSON
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("chat_id", chatId);
            jsonObject.put("text", message);
            jsonObject.put("disable_notification", "TRUE");
            jsonObject.put("parse_mode", "MarkdownV2");

            // Show JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject.toString());
            System.out.println("---==SHOW JSON==--");
            System.out.println(rootNode.toPrettyString());
            System.out.println("--==END==--");

            // Создаём URL для метода sendMessage
            URL url = new URL(API_URL + token + "/sendMessage");
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
            e.printStackTrace(System.out);
        }
    }

    public void sendPhoto(String chatId, String photoUrl) {
        try {
            URL url = new URL(API_URL + token + "/sendPhoto?chat_id=" + chatId + "&photo=" + photoUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream().close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
