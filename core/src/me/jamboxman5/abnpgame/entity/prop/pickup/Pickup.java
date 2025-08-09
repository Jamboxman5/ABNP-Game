package me.jamboxman5.abnpgame.entity.prop.pickup;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.prop.Prop;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class Pickup extends Prop {

    static Sprite defaultPickupSprite;

    protected boolean pickedUp = false;
    protected Runnable pickupEffect;
    public Pickup(Sprite pickupSprite,
                  Vector2 position,
                  float rotation,
                  Shape2D collision,
                  Runnable pickupEffect) {
        super(pickupSprite, position, rotation, collision);

        this.pickupEffect = pickupEffect;

    }

    @Override
    public void update(float delta) {
        super.update(delta);

        Polygon coll = (Polygon) getCollision();
        coll.setPosition(position.x, position.y);
        if (checkCollision(coll, ABNPGame.getInstance().getPlayer().getCollision()) && !pickedUp) {
            pickUp();
        }
    }

    public static void initSprites() {
        defaultPickupSprite = setup("entity/zombie/misc/Splatter", null);
    }

    public void pickUp() {
        pickedUp = true;
        ABNPGame.getInstance().getMapManager().disposingEntities.add(this);
        pickupEffect.run();
    }


    public boolean checkCollision(Polygon polygon, Circle circle) {
        if (Intersector.isPointInPolygon(polygon.getTransformedVertices(), 0, polygon.getTransformedVertices().length, circle.x, circle.y)) {
            return true;
        }

        float[] verts = polygon.getTransformedVertices();
        Vector2 p1 = new Vector2();
        Vector2 p2 = new Vector2();

        for (int i = 0; i < verts.length; i += 2) {
            p1.set(verts[i], verts[i + 1]);
            p2.set(verts[(i + 2) % verts.length], verts[(i + 3) % verts.length]); // wraps last edge to first
            if (Intersector.intersectSegmentCircle(p1, p2, new Vector2(circle.x, circle.y), circle.radius * circle.radius)) {
                return true;
            }
        }

        return false;
    }
}
