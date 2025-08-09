package me.jamboxman5.abnpgame.entity.prop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Pickup extends Prop {

    static Sprite pickupSprite;
    public Pickup(Vector2 position, float rotation) {
        super(pickupSprite, position, rotation);
        collision = new Circle(position, 30);
    }

    public static void initSprites() {
        pickupSprite = setup("entity/zombie/misc/Splatter", null);
    }
}
