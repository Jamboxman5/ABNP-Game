package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TextUtils {

    private static final GlyphLayout glyph = new GlyphLayout();
    public static float getStringWidth(BitmapFont font, String text) {
        glyph.setText(font, text);
        return glyph.width;
    }
    public static float getStringHeight(BitmapFont font, String text) {
        glyph.setText(font, text);
        return glyph.height;
    }
    public static void drawScaled(BitmapFont font, float scale, String text, SpriteBatch batch, float x, float y) {
        font.getData().setScale(scale);
        font.draw(batch, text, x, y);
        font.getData().setScale(1f);
    }
}
