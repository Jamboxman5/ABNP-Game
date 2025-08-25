package me.jamboxman5.abnpgame.entity.prop;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class Prop extends Entity {

    protected Decal propDecal;

    protected Prop(Sprite activeSprite,
                   Vector3 position,
                   float rotation,
                   Shape2D collision) {

        super(ABNPGame.getInstance());
        this.position = position;
        this.propDecal = Decal.newDecal(new TextureRegion(activeSprite.getTexture()), true);
        this.rotation = rotation;
        this.collision = collision;

        propDecal.setScale(activeSprite.getScaleX());
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(DecalBatch batch, ShapeRenderer shape, PerspectiveCamera camera) {
        // Make sure the batch is using your camera projection

        propDecal.setPosition(position.x, position.y, -position.z);
        propDecal.setRotationZ(getRotation());

        batch.add(propDecal);

    }

}
