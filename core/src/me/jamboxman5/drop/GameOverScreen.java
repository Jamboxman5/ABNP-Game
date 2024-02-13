package me.jamboxman5.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

class GameOverScreen implements Screen {
    final Drop drop;
    OrthographicCamera camera;
    long startTime;

    public GameOverScreen(final Drop drop) {
        this.drop = drop;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        drop.batch.setProjectionMatrix(camera.combined);
        drop.batch.begin();
        drop.font.draw(drop.batch, "Game Over!", 100, 200);
        drop.font.draw(drop.batch, "You missed too many drops!", 100, 150);
        if (System.currentTimeMillis() - startTime > 3000) drop.font.draw(drop.batch, "Touch to play again!", 100, 100);
        drop.batch.end();

        if (Gdx.input.isTouched() && System.currentTimeMillis() - startTime > 3000) {
            drop.setScreen(new MyGame(drop));
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