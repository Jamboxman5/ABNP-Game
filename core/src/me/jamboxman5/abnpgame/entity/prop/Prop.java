package me.jamboxman5.abnpgame.entity.prop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class Prop extends Entity {

    protected Sprite activeSprite;

    protected Prop(Sprite activeSprite,
                   Vector2 position,
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
        int x = (int) (((position.x - gp.getPlayer().getWorldX())*.5) + gp.getPlayer().getScreenX());
        int y = (int) (((position.y - gp.getPlayer().getWorldY())*.5) + gp.getPlayer().getScreenY());

        batch.begin();
        Sprite toDraw = activeSprite;

        batch.setTransformMatrix(new Matrix4().translate((float) x, (float) y, 0).rotate(0f, 0f, 1f, getRotation()));
        toDraw.setPosition((-toDraw.getWidth() / 2), (-toDraw.getHeight() / 2));
        toDraw.draw(batch);
        batch.setTransformMatrix(new Matrix4());


        batch.end();

    }
}
