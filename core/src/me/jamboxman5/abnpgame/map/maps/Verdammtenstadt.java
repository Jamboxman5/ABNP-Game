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
        super("Verdammtenstadt", new Vector2(1426, 1374));
        type = MapType.VERDAMMTENSTADT;
    }

    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {new Vector2(1600, 2040),
                new Vector2(1240, 1710),
                new Vector2(1790, 1500),
                new Vector2(-800, 970),
                new Vector2(190, 280)};
        return spawnPoints;
    }

    public static void setTexture(Texture texture) {
        Verdammtenstadt.texture = texture;
    }
}
