package me.jamboxman5.abnpgame.screen.ui.elements.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.jamboxman5.abnpgame.util.Fonts;

public class Button {

    Rectangle bounds;
    String text;
    BitmapFont font;
    boolean fill;
    float textScale;
    Color fillColor = new Color((75f/255f),0f,0f, .6f);
    TextAlign align;
    Runnable buttonAction;

    public Button(int x, int y, int width, int height, String text, BitmapFont font) {
        this.text = text;
        this.font = font;
        this.fill = true;
        align = TextAlign.CENTER;
        textScale = 1f;
        bounds = new Rectangle(x, y, width, height);
    }

    public Button(int x, int y, int width, int height, boolean fill) {
        this.fill = fill;
        align = TextAlign.CENTER;
        textScale = 1f;
        bounds = new Rectangle(x, y, width, height);
    }

    public void reposition(int dX, int dY) {
        bounds.x += dX;
        bounds.y += dY;
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

    public Button(int x, int y, String string, BitmapFont font, Color fillColor) {
        this(x,y,string,font, 1f);
        this.fillColor = fillColor;
        align = TextAlign.CENTER;
    }

    public Button(int x, int y, String string, BitmapFont font, Color fillColor, TextAlign align) {
        this(x,y,string,font, fillColor);
        this.align = align;
    }

    public void draw(SpriteBatch batch, ShapeRenderer renderer, boolean active) {

        if (fill) {

            renderer.begin(ShapeRenderer.ShapeType.Filled);
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            renderer.setColor(fillColor);
            renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            renderer.end();

        }

        batch.begin();

        if (text != null && !text.isEmpty()) {
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
            int y = (int) (bounds.y + (int)(bounds.height/1.6));
            if (active) font = Fonts.SELECTEDBUTTONFONT;
            else font = Fonts.BUTTONFONT;

            if (fill)
                Fonts.drawScaled(font, textScale, text, batch, x, y - (Fonts.getTextHeight("/", font, 1f)/2f));
            else Fonts.drawScaled(font, textScale, text, batch, x, y+(bounds.height/2));
        }

        batch.end();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
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

