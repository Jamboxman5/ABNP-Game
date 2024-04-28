package me.jamboxman5.steering.statemachine;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.steering.statemachine.entity.SteerGuard;
import me.jamboxman5.steering.statemachine.entity.SteerPlayer;

public class SteeringGameScreen implements Screen {

    public SteeringGame game;
    public SteerPlayer player;
    public SteerGuard guard;
    public Rectangle building;

    ShapeRenderer renderer;

    public SteeringGameScreen(SteeringGame game) {
        this.game = game;
        renderer = new ShapeRenderer();
        building = new Rectangle(1280/2 - 250, 720/2-150, 500, 300);

    }

    @Override
    public void show() {
        player = new SteerPlayer(this);
        guard = new SteerGuard(this);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.BLACK);

        player.update();
        guard.update();

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        drawBuilding();

        guard.drawVisionArc(renderer);
        player.draw(renderer);
        guard.draw(renderer);

        Vector2 target = new Vector2(player.bounds.x, player.bounds.y);
        Vector2 prediction = player.velocity.cpy();
        prediction.scl(10);
        target.add(prediction);
        renderer.circle(prediction.x, prediction.y, 10);

        renderer.end();
    }

    void drawBuilding() {
        renderer.setAutoShapeType(true);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.ORANGE);
        renderer.rect(1280/2 - 250, 720/2-150, 500, 300);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(1280/2 - 250, 720/2-150, 500, 300);
        renderer.set(ShapeRenderer.ShapeType.Filled);
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

    public void gameOver() {
        game.setScreen(new SteeringGameOverScreen(game));
        dispose();
    }
}
