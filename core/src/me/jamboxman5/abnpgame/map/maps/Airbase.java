package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Airbase extends Map {

    private static Texture texture;

    public Airbase() {
        super("Airbase", new Vector2(580, 540));
        type = MapType.AIRBASE;
    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {
                new Vector2(0, 440),
                new Vector2(250, 0),
                new Vector2(1020, 230),
                new Vector2(1020, 960),
                new Vector2(1670, 370),
                new Vector2(0, 1230),
                new Vector2(1020, 1670)
        };
        return spawnPoints;
    }

    public static void setTexture(Texture texture) {
        Airbase.texture = texture;
    }
}
