package ru.tyatyushkin.telegram;

import java.net.HttpURLConnection;
import java.net.URL;

public class Youtube {
    private final String token;
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";

    public Youtube(String token){
        this.token = token;
    }

    public String lastVideo(String channelId) {
        try {
            String urlString = String.format(
                    "https://www.googleapis.com/youtube/v3/search?key=%s&channelId=%s&part=snippet&order=date&maxResults=5",
                    token, CHANNEL_ID);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            conn.disconnect();
        } catch (Exception e) {
            LoggerConfig.logger.error("An error occurred while fetching the last video: ", e);
        }
        return null;
    }
}
