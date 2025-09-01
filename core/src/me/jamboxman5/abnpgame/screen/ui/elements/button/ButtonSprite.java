package me.jamboxman5.abnpgame.screen.ui.elements.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.jamboxman5.abnpgame.util.Fonts;

import java.awt.*;

public class ButtonSprite extends Button {

    Sprite sprite;
    float spriteScale;
    boolean spriteShadow;
    int spriteShadowOffset = 4;
    float dropShadowOpacity = .5f;

    public ButtonSprite(int x, int y, int width, int height, Sprite sprite, float spriteScale) {
        super(x, y, width, height, true);
        this.sprite = sprite;
        this.spriteScale = spriteScale;
        this.spriteShadow = false;
    }

    public ButtonSprite(int x, int y, Sprite sprite, float spriteScale) {
        super((int) (x - (sprite.getWidth() * spriteScale)),
                (int) (y - (sprite.getHeight() * spriteScale)),
                (int) (sprite.getWidth() * spriteScale),
                (int) (sprite.getHeight() * spriteScale),
                false);
        this.sprite = sprite;
        this.spriteScale = spriteScale;
        this.spriteShadow = true;

    }

    public void setSpriteDropShadow(boolean active) {
        this.spriteShadow = active;
    }

    public void addText(String text, BitmapFont font, float txtScale) {
        this.text = text;
        this.textScale = txtScale;
        this.font = font;
    }

    public void setDropShadowOffset(int offset) {
        this.spriteShadowOffset = offset;
    }

    public void setDropShadowOpacity(float opacity) {
        this.dropShadowOpacity = opacity;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapes, boolean active) {

        if (fill) {

            shapes.begin(ShapeRenderer.ShapeType.Filled);
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            if (active) {
                shapes.setColor(new Color(fillColor).add(-(20f/255f), 0, 0, 0));
            } else {
                shapes.setColor(fillColor);
            }
            shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            shapes.end();

        }

        batch.begin();

        if (sprite != null) {
            float oldScale = sprite.getScaleX();
            sprite.setCenter(bounds.x + bounds.width/2f, bounds.y + bounds.height/2f);
            sprite.setScale(spriteScale);

            if (spriteShadow) {
                sprite.setColor(0, 0, 0, dropShadowOpacity);
                sprite.translate(spriteShadowOffset, -spriteShadowOffset);
                sprite.draw(batch);
                sprite.translate(-spriteShadowOffset, spriteShadowOffset);
                sprite.setColor(Color.WHITE);
            }

            if (active) {
                sprite.setColor(.5f, .5f, .5f, .9f);
            }
            sprite.draw(batch);
            sprite.setScale(oldScale);
            sprite.setColor(1, 1, 1, 1);
        }

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

}
