package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Youtube {
    private final String token;
    private static String lastVideoID = null;

    public Youtube(String token) {
        this.token = token;
    }

    public String lastVideo(String channelId) {
        try {
            String urlString = String.format(
                    "https://www.googleapis.com/youtube/v3/search?key=%s&channelId=%s&part=snippet&order=date&maxResults=5",
                    token, channelId);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }
            reader.close();
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(content.toString());
            JsonNode items = jsonResponse.get("items");

            if (items != null && items.isArray() && !items.isEmpty()) {
                JsonNode latestVideo = items.get(items.size() - 1);
                JsonNode snippet = latestVideo.get("snippet");
                String videoId = latestVideo.get("id").get("videoId").asText();
                String title = snippet.get("title").asText();

                if (lastVideoID == null || !lastVideoID.equals(videoId)) {
                    lastVideoID = videoId;

                    // Печать информации о последнем видео
                    System.out.println("Новое видео обнаружено:");
                    System.out.println("Название последнего видео: " + title);
                    System.out.println("ID видео: " + videoId);
                    System.out.println("URL видео: https://www.youtube.com/watch?v=" + videoId);
                    return "https://www.youtube.com/watch?v=" + videoId;
                } else {
                    System.out.println("Нет новых видео.");
                }
            }
        } catch (Exception e) {
            LoggerConfig.logger.error("An error occurred while fetching the last video: ", e);
        }
        return null;
    }
}

