package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Airbase extends Map {

    private static Texture texture;

    public Airbase() {
        super("Airbase", new Vector3(580, 0, 540));
        type = MapType.AIRBASE;
    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector3[] getZombieSpawns() {
        Vector3[] spawnPoints = {
                new Vector3(0, 0, 440),
                new Vector3(250, 0, 0),
                new Vector3(1020, 0, 230),
                new Vector3(1020, 0, 960),
                new Vector3(1670, 0, 370),
                new Vector3(0, 0, 1230),
                new Vector3(1020, 0, 1670)
        };
        return spawnPoints;
    }


    public static void setTexture(Texture texture) {
        Airbase.texture = texture;
    }
}
