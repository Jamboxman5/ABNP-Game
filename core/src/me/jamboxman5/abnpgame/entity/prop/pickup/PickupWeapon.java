package me.jamboxman5.abnpgame.entity.prop.pickup;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.Weapon;

public class PickupWeapon extends Pickup {

    Weapon pickedUp;

    public PickupWeapon(final Weapon pickupWeapon, Vector2 position, float rotation) {
        super(defaultPickupSprite,
                position,
                rotation,
                generateCollision(),
                null);

        this.pickedUp = pickupWeapon;
        this.pickupEffect = new Runnable() {
            @Override
            public void run() {
                ABNPGame.getInstance().getPlayer().getWeaponLoadout().addWeapon(pickedUp, true);
            }
        };
    }

    private static Shape2D generateCollision() {
        Polygon coll = new Polygon();
        float[] verts = new float[8];

        verts[0] = -10;
        verts[1] = -10;

        verts[2] = -10;
        verts[3] = 10;

        verts[4] = 10;
        verts[5] = 10;

        verts[6] = 10;
        verts[7] = -10;

        coll.setVertices(verts);
        return coll;
    }

    public Weapon getWeapon() {
        return pickedUp;
    }
}
