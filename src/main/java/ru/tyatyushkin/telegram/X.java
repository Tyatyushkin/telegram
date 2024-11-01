package ru.tyatyushkin.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class X {
    static final String API_URL = "https://api.x.com/2/tweets";
    private final String bearerToken;

    public X(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getTweets(String query) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(API_URL + "?query=" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
            } else {
                System.err.println("HTTP error code: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
