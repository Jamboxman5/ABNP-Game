package me.jamboxman5.abnpgame.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.util.Settings;

import javax.swing.filechooser.FileSystemView;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.UUID;

public class SettingsData {

    static final String dataPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/ABNPGame/data/";
    static final String settingsFile = "settings.json";
    static final String settingsPath =  dataPath + settingsFile;

    public static void loadSettings() {

        String jsonString = null;
        try {
            InputStream is = new FileInputStream(settingsPath);
            jsonString = DataManager.load(is);
            JsonObject settingsOBJ = JsonParser.parseString(jsonString).getAsJsonObject();
            bindSettings(settingsOBJ);
        } catch (IOException e) {
            generateNewSettingsFile();
        }

    }

    @SuppressWarnings("NewApi")
    private static void generateNewSettingsFile() {
        try {
            Files.createDirectories(Paths.get(dataPath));
            InputStream defaultInput = Gdx.files.internal("data/settings/settings.json/").read();
            assert defaultInput != null;
            String json = DataManager.load(defaultInput);
            JsonObject settingsOBJ = JsonParser.parseString(json).getAsJsonObject();
            DataManager.save(settingsOBJ.toString(), settingsPath);
            bindSettings(settingsOBJ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void bindSettings(JsonObject settingsOBJ) {
        Settings.screenWidth = settingsOBJ.get("screenWidth").getAsInt();
        Settings.screenHeight = settingsOBJ.get("screenHeight").getAsInt();
        Settings.guiScale = settingsOBJ.get("guiScale").getAsFloat();
        Settings.musVolume = settingsOBJ.get("musVolume").getAsFloat();
        Settings.sfxVolume = settingsOBJ.get("sfxVolume").getAsFloat();
        Settings.hudMargin = settingsOBJ.get("hudMargin").getAsInt();
    }

    public static void updateSettings() {
        JsonObject settingsOBJ = new JsonObject();
        settingsOBJ.addProperty("screenWidth", Settings.screenWidth);
        settingsOBJ.addProperty("screenHeight", Settings.screenHeight);
        settingsOBJ.addProperty("guiScale", Settings.guiScale);
        settingsOBJ.addProperty("hudMargin", Settings.hudMargin);
        settingsOBJ.addProperty("sfxVolume", Settings.sfxVolume);
        settingsOBJ.addProperty("musVolume", Settings.musVolume);

        try {
            DataManager.save(settingsOBJ.toString(), settingsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
