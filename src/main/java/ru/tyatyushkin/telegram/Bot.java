package ru.tyatyushkin.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {
    private final String token;
    private String chatID;
    private static final String API_URL = "https://api.telegram.org/bot";
    private static int lastUpdateId = 0;

    public Bot(String token) {
        this.token = token;
    }

    public void initialize() {
        System.out.println("--==START INITIALIZE==--");
        String app_token = System.getenv("TG_TOKEN");
        String test_par = System.getenv("TEST_PAR");
        if (app_token == null) {
            System.out.println("Ошибка: Задайте значение переменной TG_TOKEN");
            return;
        } else {
            System.out.println("TG_TOKEN - прочитан");
        }
        if (test_par == null) {
            System.out.println("Ошибка: ааааа");
            System.exit(1);
        } else {
            System.out.println("TEST_PAR - прочтан");
        }
    }

    public void createTestBot() {
        initialize();
        // Подключаем бота
        Telegram telegram = new Telegram(token);
        // Тестирование расписания
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        scheduler.scheduleAtFixedRate(() -> telegram.sendMessage("47279672","*test* \n||Silent||"),0, 30,TimeUnit.SECONDS );
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
            e.printStackTrace(System.out);
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
            e.printStackTrace(System.out);
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
            e.printStackTrace(System.out);
        }
    }
}
