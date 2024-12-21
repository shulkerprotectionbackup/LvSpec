package mc.lpvania.lvspec.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public class UpdaterUtil {

    @Getter
    private String Version;
    @Getter
    private String Author;
    @Getter
    private String Telegram;
    @Getter
    private String TelegramStudio;
    @Getter
    private String Discord;
    @Getter
    private OffsetDateTime lastUpdated;

    public String url_h = "https://raw.githubusercontent.com/shulkerprotectionbackup/LvStudios/refs/heads/main/version";
    public String name_h = "LvSpec";
    public String version_h = "v0.2";

    public void init() {
        onInfo();
    }

    public void onInfo() {
        try {
            URL url = new URL(url_h);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            JsonParser parser = new JsonParser();
            JsonObject pluginData = parser.parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();

            if (pluginData.has(name_h)) {
            JsonObject lvSpecData = pluginData.getAsJsonObject(name_h);
            this.Version = lvSpecData.get("ver_new").getAsString();
            this.Author = lvSpecData.get("author").getAsString();
            this.Telegram = lvSpecData.get("telegram").getAsString();
            this.TelegramStudio = lvSpecData.get("telegram_studio").getAsString();
            this.Discord = lvSpecData.get("discord").getAsString();
            String lastUpdatedStr = lvSpecData.get("lastUpdated").getAsString();

            lastUpdatedStr = lastUpdatedStr.replaceAll("\\[UTC\\]", "");

            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            this.lastUpdated = OffsetDateTime.parse(lastUpdatedStr, formatter);
            }

//            System.out.println("Plugin Last Updated: " + lastUpdated);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTimeLastUpdate() {
        if (lastUpdated == null) {
            return "Дата обновления недоступна";
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Duration duration = Duration.between(lastUpdated, now);

        if (duration.toDays() > 0) {
            return duration.toDays() + " дня(ей) назад";
        } else if (duration.toHours() > 0) {
            return duration.toHours() + " часа(ов) назад";
        } else if (duration.toMinutes() > 0) {
            return duration.toMinutes() + " минут(ы) назад";
        } else {
            return duration.getSeconds() + " секунд назад";
        }
    }
}
