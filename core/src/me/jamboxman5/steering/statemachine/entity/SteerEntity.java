package me.jamboxman5.steering.statemachine.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.steering.statemachine.SteeringGameScreen;

public class SteerEntity {

    public Rectangle bounds;
    protected Vector2 target;
    public Vector2 orientation;
    public Vector2 velocity;
    public Vector2 acceleration;
    public Rectangle deadSpace;


    protected int speed;
    protected SteeringGameScreen screen;

    protected SteerEntity(Rectangle bounds, SteeringGameScreen screen) {
        this.bounds = bounds;
        this.screen = screen;
        velocity = new Vector2(0,0);
        acceleration = new Vector2(0,0);
        deadSpace = new Rectangle(1280/2 - 280, 720/2-180, 520, 320);

    }

    public void update() {

        orientation = velocity.nor();
        velocity.add(acceleration);
        velocity.limit(speed);

        if (!deadSpace.contains(new Vector2(bounds.x + velocity.x, bounds.y))) bounds.x += velocity.x;
        else bounds.x += velocity.y;
        if (!deadSpace.contains(new Vector2(bounds.x, bounds.y + velocity.y))) bounds.y += velocity.y;
        else bounds.y += velocity.x;

//        bounds.setPosition(new Vector2(bounds.x, bounds.y).add(velocity));

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
