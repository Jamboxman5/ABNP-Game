package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

    public static Music AMBIENCE;
    public static Sound MENUSCROLL;
    public static Sound MENUSELECT;

    public static void loadAssets(AssetManager assets) {
        assets.load("sound/music/Menu_Ambience.wav/", Music.class);
        assets.load("sound/sfx/menu/Menu_Scroll.wav/", Sound.class);
        assets.load("sound/sfx/menu/Menu_Select.wav/", Sound.class);
    }

    public static void loadSounds(AssetManager assets) {
        AMBIENCE = assets.get("sound/music/Menu_Ambience.wav/");
        MENUSCROLL = assets.get("sound/sfx/menu/Menu_Scroll.wav/");
        MENUSELECT = assets.get("sound/sfx/menu/Menu_Select.wav/");
    }

    public static void dispose() {
        AMBIENCE.dispose();

        MENUSCROLL.dispose();
        MENUSELECT.dispose();
    }

    public static void updateVolumes() {
        AMBIENCE.setVolume(Settings.musVolume);
    }

}
