package ru.tyatyushkin.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

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
        String chatId = System.getenv("CHAT_ID");
        if (app_token == null) {
            System.out.println("Ошибка: Задайте значение переменной TG_TOKEN");
            System.exit(1);
        } else {
            System.out.println("TG_TOKEN - прочитан");
        }
        if (chatId == null) {
            System.out.println("Ошибка: Задайте значение пременной CHAT_ID");
            System.exit(1);
        } else {
            System.out.println("CHAT_ID - прочтан");
            this.chatID = chatId;
        }
        System.out.println("--==END INITIALIZE==--");
    }

    public void createBot() {

    }

    public void createTestBot() {
        // Инициализация и проверка переменных
        initialize();
        // Подключаем бота
        Telegram telegram = new Telegram(token);
        // Тестирование расписания
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        Runnable morning = () -> {
            System.out.println("--==TEST SCHEDULER==--");
            telegram.sendMessage(chatID,"**ТЕСТ Сообщения по расписанию** ~~В 11:00~~");
            telegram.sendMessage(chatID,"Привет жалкие ||людишки||");
            System.out.println("--==END==--");
        };
        long initialDelay = calculateInitialDelay(zoneId);

        scheduler.scheduleAtFixedRate(morning, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        // scheduler.scheduleAtFixedRate(() -> telegram.sendMessage(chatID,"*test* \n||Silent||"),0, 30,TimeUnit.SECONDS );
    }

    public static void setLastUpdateId(int updateId) {
        lastUpdateId = updateId;
    }

    private static long calculateInitialDelay(ZoneId zoneId) {
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        ZonedDateTime nextRun = now.withHour(9).withMinute(0).withSecond(0).withNano(0);

        // Если текущее время уже прошло 10:00, установка на 10:00 следующего дня
        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }

        // Расчет задержки до следующего запуска
        return ChronoUnit.MILLIS.between(now, nextRun);
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
            } else {
                System.out.println("Error: " + responseCode + " - " + conn.getResponseMessage());
            }
            conn.disconnect();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return null;
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
