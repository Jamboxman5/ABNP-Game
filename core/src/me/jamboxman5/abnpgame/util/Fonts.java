package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.awt.*;

public class Fonts {

    public static BitmapFont TITLEFONT;
    public static BitmapFont SUBTITLEFONT;
    public static BitmapFont SELECTIONFONT;
    public static BitmapFont INFOFONT;

    private static GlyphLayout layout;

    public static void initFonts() {
        TITLEFONT = new BitmapFont(Gdx.files.internal("font/titlefont/TITLEFONT.fnt"));
        SUBTITLEFONT = new BitmapFont(Gdx.files.internal("font/subtitlefont/SUBTITLEFONT.fnt"));
        SELECTIONFONT = new BitmapFont(Gdx.files.internal("font/subtitlefont/SUBTITLEFONT.fnt"));
        INFOFONT = new BitmapFont(Gdx.files.internal("font/infofont/INFOFONT.fnt"));
        layout = new GlyphLayout();
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

    public static void drawScaled(BitmapFont font, float scale, String text, SpriteBatch batch, float x, float y) {
        font.getData().setScale(scale);
        font.draw(batch, text, x, y);
        font.getData().setScale(1f);
    }

    public static float getXForCenteredText(int centeredX, String text, BitmapFont font) {
        layout.setText(font, text);
        float length = layout.width;
        return centeredX - length / 2;
    }
    public static float getXForRightAlignedText(int rightX, String text, BitmapFont font, Float scale) {
        if (scale == null) {
            layout.setText(font, text);
            float length = layout.width;font.getData().setScale(1f);
            return rightX - length;
        } else {
            font.getData().setScale(scale);
            layout.setText(font, text);
            float length = layout.width;font.getData().setScale(1f);
            font.getData().setScale(1f);
            return rightX - length;
        }

    }
    public static float getTextHeight(String text, BitmapFont font, float scale) {
        font.getData().setScale(scale);
        layout.setText(font, text);
        float height = layout.height;
        font.getData().setScale(1f);
        return height;
    }

    public static float getTextWidth(String text, BitmapFont font) {
        layout.setText(font, text);
        return layout.width;
    }

    public static void dispose() {
        TITLEFONT.dispose();
        SUBTITLEFONT.dispose();
        SELECTIONFONT.dispose();
        INFOFONT.dispose();
    }

}
