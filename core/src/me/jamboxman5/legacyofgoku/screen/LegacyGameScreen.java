package me.jamboxman5.legacyofgoku.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.jamboxman5.legacyofgoku.LegacyGame;
import me.jamboxman5.legacyofgoku.map.LegacyMap;
import me.jamboxman5.legacyofgoku.map.LegacyMapManager;

public class LegacyGameScreen implements Screen {

    final LegacyGame game;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;
    LegacyMapManager mapManager;

    public LegacyGameScreen(LegacyGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
        mapManager = new LegacyMapManager(game);
    }

    @Override
    public void show() {

        mapManager.setActiveMap(LegacyMap.MapLevel.STARTMAP);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        update();
        draw();

    }

    private void draw() {

        game.batch.begin();
        mapManager.draw(game.batch);
        game.batch.end();

    }

    private void update() {



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
