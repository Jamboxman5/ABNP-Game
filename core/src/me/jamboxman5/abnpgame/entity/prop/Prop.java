package me.jamboxman5.abnpgame.entity.prop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class Prop extends Entity {

    protected Sprite activeSprite;

    protected Prop(Sprite activeSprite,
                   Vector3 position,
                   float rotation,
                   Shape2D collision) {

        super(ABNPGame.getInstance());
        this.position = position;
        this.activeSprite = activeSprite;
        this.rotation = rotation;
        this.collision = collision;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        // Make sure the batch is using your camera projection

        batch.begin();

        Sprite toDraw = activeSprite;

        // Translate to world position, rotate around center
        batch.setTransformMatrix(
                new Matrix4()
                        .translate(position.x, position.y, 0)
                        .rotate(0f, 0f, 1f, getRotation())
        );

        // Draw sprite centered on its position
        toDraw.setPosition(-toDraw.getWidth() / 2f, -toDraw.getHeight() / 2f);
        toDraw.draw(batch);

        // Reset transform so later things don’t inherit it
        batch.setTransformMatrix(new Matrix4());

        batch.end();
    }

}
