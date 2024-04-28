package me.jamboxman5.statemachine.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.jamboxman5.statemachine.StateMachineGameScreen;

public class SMPlayer extends SMEntity {

    public SMPlayer(StateMachineGameScreen screen) {
        super(new Rectangle(40, 40, 40, 40), screen);
        speed = 10;
    }

    public void draw(ShapeRenderer renderer) {
        super.draw(renderer, Color.BLUE);
    }

    @Override
    public void update() {
        super.update();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            move(0, speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(-speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(0, -speed);
        }



    }



}
