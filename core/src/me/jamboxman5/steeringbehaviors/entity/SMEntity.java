package me.jamboxman5.steeringbehaviors.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SMEntity {

    private Rectangle bounds;
    private Vector2 target;

    public void draw(ShapeRenderer renderer) {
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
