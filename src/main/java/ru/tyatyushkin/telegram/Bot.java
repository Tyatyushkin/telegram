package ru.tyatyushkin.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class Bot {
    private final String token;
    private String chatID;
    private static final String API_URL = "https://api.telegram.org/bot";
    private static int lastUpdateId = 0;

    public Bot(String token) {
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
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public void sendMessage(String chatId, String message) {
        this.chatID = chatId;
        try {
            URL url = new URL(API_URL + token + "/sendMessage?chat_id=" + chatID + "&text=" + message);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream().close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(String chatId, String photoUrl) {
        this.chatID = chatId;
        try {
            URL url = new URL(API_URL + token + "/sendPhoto?chat_id=" + chatID + "&photo=" + photoUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream().close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
