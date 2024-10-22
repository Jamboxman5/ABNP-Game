package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;

public class Karnivale extends Map {

    private static final FileHandle texture = (Gdx.files.internal("map/" + "Karnivale" + ".png/"));

    public Karnivale() {
        super("Karnivale", new Vector2(-315, 850));
    }

    @Override
    public void load() {
        img = new Sprite(new Texture(texture));
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {new Vector2(-1000, 2000),
                new Vector2(25, 2000),
                new Vector2(-250, -1500),
                new Vector2(-2300, 250),
                new Vector2(1750, 550)};
        return spawnPoints;
    }
}
