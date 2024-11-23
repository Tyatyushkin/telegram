package ru.tyatyushkin.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


public class Bot {
    private final String token;
    private String chatID;
    private String weath;
    private String w_token;

    public Bot(String token) {
        this.token = token;
    }

    public void initialize() {
        LoggerConfig.logger.info("--==START VARS INITIALIZE==--");
        String app_token = System.getenv("TG_TOKEN");
        String chatId = System.getenv("CHAT_ID");
        String x_token = System.getenv("X_TOKEN");
        String x_username = System.getenv("X_USERNAME");
        String w_token = System.getenv("W_TOKEN");
        if (app_token == null) {
            LoggerConfig.logger.error("Ошибка: Задайте значение переменной TG_TOKEN");
            System.exit(1);
        } else {
            LoggerConfig.logger.info("TG_TOKEN - прочитан");
        }
        if (chatId == null) {
            LoggerConfig.logger.error("Ошибка: Задайте значение переменной CHAT_ID");
            System.exit(1);
        } else {
            LoggerConfig.logger.info("CHAT_ID - прочитан");
            this.chatID = chatId;
        }
        if (x_token == null) {
            LoggerConfig.logger.error("Ошибка: Задайте значение переменной X_TOKEN");
            System.exit(1);
        }
        else {
            LoggerConfig.logger.info("X_TOKEN - прочитан");
        }
        if (x_username == null) {
            LoggerConfig.logger.error("Ошибка: Задайте значение переменной X_USERNAME");
            System.exit(1);
        } else {
            LoggerConfig.logger.info("X_USERNAME - прочитан");
        }
        if (w_token == null) {
            LoggerConfig.logger.error("Ошибка: Задайте значение переменной W_TOKEN");
            System.exit(1);
        } else {
            LoggerConfig.logger.info("W_TOKEN - прочитан");
            this.w_token = w_token;
        }
        LoggerConfig.logger.info("--==END INITIALIZE==--");
    }

    public void createBot() {
        initialize();
        Telegram telegram = new Telegram(token);
        Scheduler scheduler = new Scheduler();

        Runnable getUpdates = () -> {
            try {
                String updates = telegram.getUpdates();
                if (updates != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(updates);
                    JsonNode resultArray = jsonNode.get("result");
                    for (JsonNode update : resultArray) {
                        int updateId = update.get("update_id").asInt();
                        JsonNode messageNode = update.get("message");
                        if (messageNode != null && messageNode.get("text") != null) {
                            String text = messageNode.get("text").asText();
                            String chatId = messageNode.get("chat").get("id").asText();

                            if (text.toLowerCase().startsWith("gpt")) {
                                telegram.sendMessage(chatId, "Адвокат когда ты уже сделаешь меня умным?");
                            }
                            if (text.toLowerCase().startsWith("хуй")) {
                                telegram.sendMessage(chatId, "трусы свои пожуй!");
                            }
                            if (text.toLowerCase().contains("python")) {
                                telegram.sendMessage(chatId, "Кому, что а ебуняке лишь бы питона душить");
                            }
                            if (text.toLowerCase().startsWith("red")) {
                                telegram.sendMessage(chatId, "Рыжие ушли на покой, попробуй написать нюдсы");
                            }
                            if (text.toLowerCase().startsWith("нюдсы")) {
                                telegram.sendPhoto(chatId, "https://pbs.twimg.com/media/GVjuLO-WwAAzjU_?format=jpg&name=large");
                            }
                        }
                        telegram.setLastUpdateId(updateId);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        };
        //TODO добавить интеграцию с X
        scheduler.addTaskDaily(() -> telegram.sendMessage(chatID,"Утро, мешки с костями!"), 7, 0);
        scheduler.addTaskAtFixedRate(getUpdates, 0, 5, TimeUnit.SECONDS);
    }

    public void createTestBot() {
        // Инициализация и проверка переменных
        initialize();
        // Подключаем бота
        Telegram telegram = new Telegram(token);
        // Подключаем модуль с погодой
        Weather weather = new Weather(w_token);
        // Создаем новый планировщик
        Scheduler scheduler = new Scheduler();
        scheduler.addTaskAtFixedRate(() -> weath = weather.getWeather(), 0, 3, TimeUnit.HOURS );
        scheduler.addTaskDaily(() -> telegram.sendMessage(chatID, "Пиздуйте спать, жалкие людишки"), 20, 0);
        scheduler.addTaskAtFixedRate(() -> {
            try {
                processUpdates(telegram.getUpdates(), telegram);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, 0, 5, TimeUnit.SECONDS);
    }

    public void processUpdates(String getUpdates, Telegram telegram){
        //Попробовать переписать на JSON
        if (getUpdates != null) {
            JSONObject json = new JSONObject(getUpdates);
            if (json.getBoolean("ok")) {
                JSONArray results = json.getJSONArray("result");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject updates = results.getJSONObject(i);
                    if (updates.has("message")) {
                        JSONObject message = updates.getJSONObject("message");
                        if (message.has("text")) {
                            String text = message.getString("text");
                            System.out.println(text);
                            long chat = message.getJSONObject("chat").getLong("id");
                            String ch = Long.toString(chat);
                            System.out.println(chat);

                            if (text.startsWith("json")) {
                                telegram.sendMessage(ch,"тест новой обработки сообщений");
                            }
                            if (text.startsWith("/test")) {
                                telegram.sendInlineButton(ch);
                            }
                            if (text.startsWith("/menu")) {
                                telegram.sendReplyButton(ch);
                            } else if (text.equals("test")) {
                                telegram.sendMessage(ch, "Проверка reply button");
                            } else if (text.equals("boobs")) {
                                telegram.sendPhoto(ch,"https://64.media.tumblr.com/ff05749b6c4319b01aa4266e62bba191/9540d1c5f001612f-ed/s400x600/bd4cd96e60106569ab2e0b6c9f5a3c5afa9903aa.jpg" );
                            } else if (text.equals("погода")) {
                                telegram.sendMessage(ch, weath );
                            }
                        }
                    } else if (updates.has("callback_query")) {
                        JSONObject callbackQuery = updates.getJSONObject("callback_query");
                        String callbackData = callbackQuery.getString("data");
                        String ch = Long.toString(callbackQuery.getJSONObject("message").getJSONObject("chat").getLong("id"));

                        if (callbackData.equals("info")) {
                            telegram.sendMessage(ch, "Я бот для тестирования");
                        }
                        if (callbackData.equals("vpn")) {
                            telegram.sendMessage(ch, "Если хотите приобрести VPN, обращайтесь к @mplane");
                        }
                        
                    }
                }
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(getUpdates);
                JsonNode resultArray = jsonNode.get("result");

                for (JsonNode update : resultArray) {
                    int updateId = update.get("update_id").asInt();
                    JsonNode messageNode = update.get("message");
                    if (messageNode != null && messageNode.get("text") != null) {
                        String text = messageNode.get("text").asText();
                        String chatId = messageNode.get("chat").get("id").asText();

                        if (text.toLowerCase().contains("тест")) {
                            telegram.sendMessage(chatId, "Что мудила криворукая ничего с первого раза сделать не можешь\\?");
                        }
                        if (text.toLowerCase().contains("сиськи")) {
                            telegram.sendPhoto(chatId, "https://64.media.tumblr.com/ff05749b6c4319b01aa4266e62bba191/9540d1c5f001612f-ed/s400x600/bd4cd96e60106569ab2e0b6c9f5a3c5afa9903aa.jpg");
                        }
                    }
                    telegram.setLastUpdateId(updateId);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
}
