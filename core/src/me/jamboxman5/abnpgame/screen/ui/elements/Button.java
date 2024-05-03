package me.jamboxman5.abnpgame.screen.ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.jamboxman5.abnpgame.util.Fonts;

import java.awt.*;


public class Button {

    Rectangle bounds;
    String text;
    BitmapFont font;
    boolean fill;
    float textScale;
    Color color;
    TextAlign align = TextAlign.CENTER;
    Runnable buttonAction;

    public Button(int x, int y, int width, int height, String text, BitmapFont font) {
        this.text = text;
        this.font = font;
        this.fill = true;
        align = TextAlign.CENTER;
        textScale = 1f;
        bounds = new Rectangle(x, y, width, height);
    }

    public Button(int x, int y, String text, BitmapFont font, float txtScale) {
        this.text = text;
        this.font = font;
        this.fill = false;
        align = TextAlign.CENTER;
        textScale = txtScale;
        bounds = new Rectangle(x, y, Fonts.getTextWidth(text, Fonts.SELECTIONFONT, txtScale), Fonts.getTextHeight(text, Fonts.SELECTIONFONT, txtScale));
    }

    public Button(int x, int y, String text, BitmapFont font, TextAlign align) {
        this.text = text;
        this.font = font;
        this.fill = false;
        this.align = align;
        textScale = 1f;
        bounds = new Rectangle(x, y, Fonts.getTextWidth(text, font, textScale), Fonts.getTextHeight(text, Fonts.SELECTIONFONT, textScale));
    }

    public Button(int x, int y, String string, BitmapFont font, Color color) {
        this(x,y,string,font, 1f);
        this.color = color;
        align = TextAlign.CENTER;
    }

    public Button(int x, int y, String string, BitmapFont font, Color color, TextAlign align) {
        this(x,y,string,font, color);
        this.align = align;
    }

    public void draw(SpriteBatch batch, ShapeRenderer renderer, boolean active) {

        if (fill) {

            renderer.begin(ShapeRenderer.ShapeType.Filled);
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            renderer.setColor(new Color(75f,0f,0f, .6f));
            renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            renderer.end();

        }


        batch.begin();
        int x = 0;
        switch (align) {
            case LEFT:
                x = (int) bounds.x;
                break;
            case RIGHT:
                x = (int) Fonts.getXForRightAlignedText((int) (bounds.x + bounds.width), text, font, textScale);
                break;
            default:
                x = (int) Fonts.getXForCenteredText((int) (bounds.x + bounds.width/2), text, font);
                break;
        }
        int y = (int) (bounds.y + (int)(bounds.height/1.7));
//        Color color = Color.WHITE;
        if (active) font = Fonts.SELECTEDBUTTONFONT;
        else font = Fonts.BUTTONFONT;
//        if (this.color != null) color = this.color;
        Fonts.drawScaled(font, textScale, text, batch, x, y+bounds.height/2);
        batch.end();
    }

    public boolean contains(Vector2 point) {
        return bounds.contains(point);
    }
    public boolean contains(int x, int y) {
        return bounds.contains(x, y);
    }

    public void setAction(Runnable action) { buttonAction = action; }
    public void press() { if (buttonAction != null) buttonAction.run(); }

    public enum TextAlign {
        CENTER, RIGHT, LEFT;
    }

}

