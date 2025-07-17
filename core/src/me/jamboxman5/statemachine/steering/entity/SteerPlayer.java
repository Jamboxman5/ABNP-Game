package me.jamboxman5.statemachine.steering.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.statemachine.steering.SteeringGameScreen;

public class SteerPlayer extends SteerEntity {

    public SteerPlayer(SteeringGameScreen screen) {
        super(new Rectangle(40, 40, 40, 40), screen);
        speed = 10;
    }

    public void draw(ShapeRenderer renderer) {
        super.draw(renderer, Color.BLUE);
    }

    @Override
    public void update() {
        super.update();

        velocity = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//            move(0, speed);
            acceleration.add(0, .5f);
        } else {
            if (acceleration.y > 0) acceleration.y -= .5f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            move(-speed, 0);
            acceleration.add(-.5f, 0);
        } else {
            if (acceleration.x < 0) acceleration.x += .5f;

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            move(speed, 0);
            acceleration.add(.5f, 0);
        } else {
            if (acceleration.x > 0) acceleration.x -= .5f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//            move(0, -speed);
            acceleration.add(0, -.5f);

        } else {
            if (acceleration.y < 0) acceleration.y += .5f;
        }

        if (acceleration.len() < .5f) acceleration.setLength(0);

        acceleration.limit(speed);



    }



}
