package ru.tyatyushkin.telegram;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Weather {
    private static String token;

    private static final Map<String, String> conditionTranslations = new HashMap<>();

    static {
        // Инициализируем карту переводов
        conditionTranslations.put("clear", "Ясно");
        conditionTranslations.put("partly-cloudy", "Малооблачно");
        conditionTranslations.put("cloudy", "Облачно с прояснениями");
        conditionTranslations.put("overcast", "Пасмурно");
        conditionTranslations.put("drizzle", "Морось");
        conditionTranslations.put("light-rain", "Небольшой дождь");
        conditionTranslations.put("rain", "Дождь");
        conditionTranslations.put("heavy-rain", "Сильный дождь");
        conditionTranslations.put("showers", "Ливень");
        conditionTranslations.put("wet-snow", "Дождь со снегом");
        conditionTranslations.put("light-snow", "Небольшой снег");
        conditionTranslations.put("snow", "Снег");
        conditionTranslations.put("snow-showers", "Снегопад");
        conditionTranslations.put("hail", "Град");
        conditionTranslations.put("thunderstorm", "Гроза");
        conditionTranslations.put("thunderstorm-with-rain", "Дождь с грозой");
        conditionTranslations.put("thunderstorm-with-hail", "Гроза с градом");
    }

    private static String translateCondition(String condition) {
        return conditionTranslations.getOrDefault(condition, "Неизвестное состояние");
    }

    public Weather(String w_token) {
        this.token = w_token;
    }

    public String getWeather() {
        try {
            StringBuilder content = new StringBuilder();
            //https://yandex.ru/pogoda/?lat=53.40716171&lon=58.98028946&via=srp
            URL url = new URL("https://api.weather.yandex.ru/v2/forecast?lat=53.40716171&lon=58.98028946&lang=ru_RU");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-Weather-Key", token);
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
            } else {
                System.out.println("Error: " + responseCode + " - " + connection.getResponseMessage());
            }
            connection.disconnect();
            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONObject fact = jsonResponse.getJSONObject("fact");

            double temp = fact.getDouble("temp");
            String condition = fact.getString("condition");
            String translateCondition = translateCondition(condition);
            double windSpeed = fact.getDouble("wind_speed");

            String weather = "DEMO режим погоды от Yandex, работает 7 дней\n Температура в Магнитогорске: " + temp
                    + ", скорость ветра: " + windSpeed + ", Состояние: " + translateCondition;
            return weather;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}
