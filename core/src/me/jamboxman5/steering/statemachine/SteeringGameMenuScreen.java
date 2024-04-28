package me.jamboxman5.steering.statemachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

class SteeringGameMenuScreen implements Screen {
    final SteeringGame game;
    OrthographicCamera camera;
    long lastNewStar;
    Array<Vector3> stars = new Array<>();
    ShapeRenderer shapeRenderer;
    public SteeringGameMenuScreen(final SteeringGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

    }

    void renderStars() {
        Gdx.gl.glEnable(GL30.GL_BLEND);

        if (System.currentTimeMillis() - lastNewStar > 50) {
            lastNewStar = System.currentTimeMillis();
            int newX = (int) (Math.random()*1280);
            int newY = (int) (Math.random()*720);

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
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();



        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderStars();
        shapeRenderer.end();
        game.batch.begin();
        game.font.draw(game.batch, "Don't let the guard catch you!", 100, 150);
        game.font.draw(game.batch, "Click anywhere to start!", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new SteeringGameScreen(game));
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