package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Settings {

    //SOUND SETTINGS
    public static float sfxVolume = .15f;
    public static float musVolume = .15f;

    //DISPLAY SETTINGS
    public static int screenWidth = 1366;
    public static int screenHeight = 768;
    public static float guiScale = 1.5f;

    public static float minZoom = 300 * guiScale;
    public static float maxZoom = 200 * guiScale;

    public static float hudMargin = 20;

    public static Array<Vector2> resolutions = getAllResolutions();

    public static void getAvailableResolutions(Vector2 screenRes) {
        Array<Vector2> allResolutions = getAllResolutions();
        resolutions = new Array<>();
        for (Vector2 res : allResolutions) {
            if (res.x <= screenRes.x && res.y <= screenRes.y) resolutions.add(res);
        }
    }

    private static Array<Vector2> getAllResolutions() {
        return new Array<>(new Vector2[]{
                new Vector2(1024, 768),
                new Vector2(1280, 720),
                new Vector2(1280, 800),
                new Vector2(1280, 960),
                new Vector2(1366, 768),
                new Vector2(1440, 900),
                new Vector2(1600, 900),
                new Vector2(1600, 1200),
                new Vector2(1680, 1050),
                new Vector2(1920, 1080),
                new Vector2(1920, 1200),
                new Vector2(2560, 1080),
                new Vector2(2560, 1440),
                new Vector2(2560, 1600),
                new Vector2(3200, 1800),
                new Vector2(3280, 2160),
                new Vector2(3440, 1440),
                new Vector2(3840, 1080),
                new Vector2(3840, 1600),
                new Vector2(5120, 1440),
                new Vector2(5120, 2160),
                new Vector2(5120, 2880),
                new Vector2(7680, 4320),
        });
    }

    public static Vector2 getResolution() {
        for (Vector2 res : resolutions) {
            if (res.x == screenWidth && res.y == screenHeight) return res;
        }
        return new Vector2(-1, -1);
    }

    public static void setResolution(Vector2 res) {
        if ((screenWidth == res.x && screenHeight == res.y) && (Gdx.graphics.getWidth() == res.x && Gdx.graphics.getHeight() == res.y)) return;
        Gdx.graphics.setUndecorated(false);
        screenWidth = Math.round(res.x);
        screenHeight = Math.round(res.y);
        Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
        if (Gdx.graphics.getWidth() != screenWidth || Gdx.graphics.getHeight() != screenHeight) {
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
        }
    }
}
