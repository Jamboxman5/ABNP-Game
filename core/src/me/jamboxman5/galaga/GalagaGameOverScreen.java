package me.jamboxman5.galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.drop.Drop;
import me.jamboxman5.drop.MyGame;

import java.util.Iterator;

class GalagaGameOverScreen implements Screen {
    final GalagaGame game;
    OrthographicCamera camera;
    long startTime;
    long lastNewStar;
    Array<Vector3> stars = new Array<>();
    ShapeRenderer shapeRenderer;

    public GalagaGameOverScreen(final GalagaGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        shapeRenderer = new ShapeRenderer();

    }

    void renderStars() {
        Gdx.gl.glEnable(GL30.GL_BLEND);

        if (System.currentTimeMillis() - lastNewStar > 50) {
            lastNewStar = System.currentTimeMillis();
            int newX = (int) (Math.random()*800);
            int newY = (int) (Math.random()*600);

            stars.add(new Vector3(newX, newY, 1f));
        }

        Iterator<Vector3> iter = stars.iterator();
        while (iter.hasNext()) {
            Vector3 star = iter.next();
            shapeRenderer.setColor(new Color(1f, 1f,1f,star.z));

            shapeRenderer.rect(star.x, star.y, 4, 4);
            star.z -= .01;
            if (star.z < 0) {
                iter.remove();
            }
        }

    }

    @Override
    public void show() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderStars();
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Game Over!", 100, 200);
        game.font.draw(game.batch, "Out of Lives!", 100, 150);
        if (System.currentTimeMillis() - startTime > 3000) game.font.draw(game.batch, "Insert Credit to play again!", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched() && System.currentTimeMillis() - startTime > 3000) {
            game.setScreen(new GalagaGameScreen(game));
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