package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Verdammtenstadt extends Map {

    private static Texture texture;

    public Verdammtenstadt() {
        super("Verdammtenstadt", new Vector3(1454, 0, 1042));
        type = MapType.VERDAMMTENSTADT;
    }

    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector3[] getZombieSpawns() {
        Vector3[] spawnPoints = {
                new Vector3(1187, 0, 1513),
                new Vector3(1496, 0, 1403),
                new Vector3(1313, 0, 1243),
                new Vector3(1580, 0, 1138),
                new Vector3(771, 0, 511),
                new Vector3(463, 0, 1738),
                new Vector3(281, 0, 868)
        };
        return spawnPoints;
    }

    public static void setTexture(Texture texture) {
        Verdammtenstadt.texture = texture;
    }
}
