package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {

    public static BitmapFont TITLEFONT;
    public static BitmapFont SUBTITLEFONT;
    public static BitmapFont SELECTIONFONT;
    public static BitmapFont INFOFONT;

    public static void initFonts() {
        TITLEFONT = new BitmapFont(Gdx.files.internal("font/titlefont/TITLEFONT.fnt"));
        SUBTITLEFONT = new BitmapFont(Gdx.files.internal("font/subtitlefont/SUBTITLEFONT.fnt"));
    }

    public static BitmapFont createFont(FileHandle fontHandle, int size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/SASFONT.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        return font;
    }

    public static void dispose() {
        TITLEFONT.dispose();
        SUBTITLEFONT.dispose();
        SELECTIONFONT.dispose();
        INFOFONT.dispose();
    }

}
