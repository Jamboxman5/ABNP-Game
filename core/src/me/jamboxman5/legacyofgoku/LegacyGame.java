package me.jamboxman5.legacyofgoku;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.jamboxman5.legacyofgoku.screen.LegacyStartMenuScreen;

public class LegacyGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        setScreen(new LegacyStartMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}
