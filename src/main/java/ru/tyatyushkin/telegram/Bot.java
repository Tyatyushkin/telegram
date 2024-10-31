package ru.tyatyushkin.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class Bot {
    private final String token;
    private static final String API_URL = "https://api.telegram.org/bot";

    public Bot(String token) {
        this.token = token;
    }

    public String getUpdates() throws Exception {
        URL url = new URL(API_URL + token + "/getUpdates");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();

        return content.toString();
    }

}
