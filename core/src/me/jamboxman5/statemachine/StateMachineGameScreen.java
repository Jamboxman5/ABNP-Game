package me.jamboxman5.statemachine;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.statemachine.entity.SMGuard;
import me.jamboxman5.statemachine.entity.SMPlayer;

public class StateMachineGameScreen implements Screen {

    public StateMachineGame game;
    public SMPlayer player;
    public SMGuard guard;
    public Rectangle building;

    ShapeRenderer renderer;

    public StateMachineGameScreen(StateMachineGame game) {
        this.game = game;
        renderer = new ShapeRenderer();
        building = new Rectangle(1280/2 - 250, 720/2-150, 500, 300);

    }

    @Override
    public void show() {
        player = new SMPlayer(this);
        guard = new SMGuard(this);
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
        game.setScreen(new StateMachineGameOverScreen(game));
        dispose();
    }
}
