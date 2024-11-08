package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public Bot(String token) {
        this.token = token;
    }
    //TODO добавить инициализацию всех переменных

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
        initialize();
        Telegram telegram = new Telegram(token);
        Runnable check = () -> {
            String updates = telegram.getUpdates();
            if (updates != null) {

            }

        };
        scheduler.scheduleAtFixedRate(check, 0, 60, TimeUnit.SECONDS);
    }

    public void createTestBot() {
        // Инициализация и проверка переменных
        initialize();
        // Подключаем бота
        Telegram telegram = new Telegram(token);
        // Тестирование расписания
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        Runnable morning = () -> {
            System.out.println("--==TEST SCHEDULER==--");
            telegram.sendMessage(chatID,"**ТЕСТ Сообщения по расписанию** ~~В 11:00~~");
            telegram.sendMessage(chatID,"Привет жалкие ||людишки||");
            System.out.println("--==END==--");
        };

        Runnable getUpdates = () -> {
            try {
                String updates = telegram.getUpdates();
                if (updates != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(updates);
                    JsonNode resultArray = jsonNode.get("result");
                    //System.out.println(resultArray.toPrettyString());
                    for (JsonNode update : resultArray) {
                        int updateId = update.get("update_id").asInt();
                        JsonNode messageNode = update.get("message");
                        if (messageNode != null && messageNode.get("text") != null) {
                            String text = messageNode.get("text").asText();
                            String chatId = messageNode.get("chat").get("id").asText();

                            if (text.toLowerCase().startsWith("хуй")) {
                                System.out.println("хуй");
                                telegram.sendMessage(chatId, "Трусы свои пожуй\\!");
                            }
                            if (text.toLowerCase().contains(" ебуняка ")) {
                                System.out.println("ебуняка");
                                telegram.sendMessage(chatId, "опять эта хитрая рожа что-то задумала");
                            }
                        }
                        Telegram.setLastUpdateId(updateId);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        };

        long initialDelay = calculateInitialDelay(zoneId);

        scheduler.scheduleAtFixedRate(morning, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(getUpdates, 0, 5, TimeUnit.SECONDS);

    }


    private static long calculateInitialDelay(ZoneId zoneId) {
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        ZonedDateTime nextRun = now.withHour(9).withMinute(0).withSecond(0).withNano(0);

        // Если текущее время уже прошло 9:00, установка на 9:00 следующего дня
        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }

        // Расчет задержки до следующего запуска
        return ChronoUnit.MILLIS.between(now, nextRun);
    }

}
