package me.jamboxman5.statemachine.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.statemachine.StateMachineGameScreen;

public class SMEntity {

    protected Rectangle bounds;
    protected Vector2 target;
    protected Vector2 orientation;
    protected Vector2 velocity;
    protected int speed;
    protected StateMachineGameScreen screen;

    protected SMEntity(Rectangle bounds, StateMachineGameScreen screen) {
        this.bounds = bounds;
        this.screen = screen;
    }

    public void update() {

        if (bounds.x < 0-bounds.width) {
            bounds.x = Gdx.graphics.getWidth() + bounds.width;
        }
        if (bounds.x > Gdx.graphics.getWidth() + bounds.width) {
            bounds.x = 0-bounds.width;
        }

        if (bounds.y < 0-bounds.height) {
            bounds.y = Gdx.graphics.getHeight() + bounds.height;
        }
        if (bounds.y > Gdx.graphics.getHeight() + bounds.height) {
            bounds.y = 0-bounds.height;
        }

    }

    protected void draw(ShapeRenderer renderer, Color color) {
        renderer.setAutoShapeType(true);
        renderer.setColor(color);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        renderer.set(ShapeRenderer.ShapeType.Filled);

    }

    protected void move(int x, int y) {
        float oldX = bounds.x;
        float oldY = bounds.y;
        bounds.x += x;
        if (bounds.overlaps(screen.building)) bounds.x = oldX;
        bounds.y += y;
        if (bounds.overlaps(screen.building)) bounds.y = oldY;

    }

}
