package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Verdammtenstadt extends Map {

    private static Texture texture;

    public Verdammtenstadt() {
        super("Verdammtenstadt", new Vector2(1454, 1042));
        type = MapType.VERDAMMTENSTADT;
    }

    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {
                new Vector2(1187, 1513),
                new Vector2(1496, 1403),
                new Vector2(1313, 1243),
                new Vector2(1580, 1138),
                new Vector2(771, 511),
                new Vector2(463, 1738),
                new Vector2(281, 868)
        };
        return spawnPoints;
    }

    public static void setTexture(Texture texture) {
        Verdammtenstadt.texture = texture;
    }
}
