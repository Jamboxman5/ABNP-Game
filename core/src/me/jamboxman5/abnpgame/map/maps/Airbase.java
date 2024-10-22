package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;

public class Airbase extends Map {

    private static final FileHandle texture = (Gdx.files.internal("map/" + "Airbase" + ".png/"));

    public Airbase() {
        super("Airbase", new Vector2(-220, 330));

    }

    @Override
    public void load() {
        img = new Sprite(new Texture(texture));
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {new Vector2(-1820, 130),
                new Vector2(160, -1550),
                new Vector2(1300, 1080),
                new Vector2(200, 3150),
                new Vector2(-2200, 2300)};
        return spawnPoints;
    }
}
