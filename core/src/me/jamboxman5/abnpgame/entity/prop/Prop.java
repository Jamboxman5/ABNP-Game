package me.jamboxman5.abnpgame.entity.prop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class Prop extends Entity {

    protected Sprite activeSprite;

    protected Prop(Sprite activeSprite,
                   Vector2 position,
                   float rotation) {

        super(ABNPGame.getInstance());
        this.position = position;
        this.activeSprite = activeSprite;
        this.rotation = rotation;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {

    }
}
