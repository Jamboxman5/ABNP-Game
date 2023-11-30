package me.jamboxman5.abnpgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Fonts;

public class MainMenuScreen implements Screen {

    final ABNPGame game;

    OrthographicCamera camera;

    public MainMenuScreen(final ABNPGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0, 0f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        Fonts.TITLEFONT.draw(game.batch, "ABNP:", 20, ScreenInfo.HEIGHT - 20);
        Fonts.drawScaled(Fonts.SUBTITLEFONT, .85f, "Zombie Assault", game.batch,20, ScreenInfo.HEIGHT - Fonts.TITLEFONT.getScaleY() - Fonts.SUBTITLEFONT.getScaleY() - 220);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
