package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

    public static Music AMBIENCE;
    public static Sound MENUSCROLL;
    public static Sound MENUSELECT;

    public static void initSounds() {
        AMBIENCE = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Menu_Ambience.wav/"));
        MENUSCROLL = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Menu_Scroll.wav/"));
        MENUSELECT = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Menu_Select.wav/"));
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
