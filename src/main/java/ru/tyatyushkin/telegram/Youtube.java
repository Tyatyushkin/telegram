package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Youtube {
    private final String token;

    public Youtube(String token){
        this.token = token;
    }

    public void lastVideo(String channelId) {
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

            for (JsonNode item : items) {
                JsonNode snippet = item.get("snippet");
                String videoId = item.get("id").get("videoId").asText();
                String title = snippet.get("title").asText();
                String description = snippet.get("description").asText();
                String publishedAt = snippet.get("publishedAt").asText();

                System.out.println("Video ID: " + videoId);
                System.out.println("Title: " + title);
                System.out.println("Description: " + description);
                System.out.println("Published At: " + publishedAt);
                System.out.println();
            }
        } catch (Exception e) {
            LoggerConfig.logger.error("An error occurred while fetching the last video: ", e);
        }
    }
}
