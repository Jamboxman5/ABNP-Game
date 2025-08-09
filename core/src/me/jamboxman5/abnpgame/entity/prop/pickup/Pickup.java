package me.jamboxman5.abnpgame.entity.prop.pickup;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.prop.Prop;

public class Pickup extends Prop {

    static Sprite defaultPickupSprite;

    protected boolean pickedUp = false;
    protected Runnable pickupEffect;
    public Pickup(Sprite pickupSprite,
                  Vector2 position,
                  float rotation,
                  Shape2D collision,
                  Runnable pickupEffect) {
        super(defaultPickupSprite, position, rotation, collision);

        this.pickupEffect = pickupEffect;

    }

    public static void initSprites() {
        defaultPickupSprite = setup("entity/zombie/misc/Splatter", null);
    }

    public void pickUp() {
        pickedUp = true;
        pickupEffect.run();
    }
}
