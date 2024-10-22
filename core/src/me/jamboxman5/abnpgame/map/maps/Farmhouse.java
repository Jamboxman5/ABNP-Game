package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;

public class Farmhouse extends Map {

    private static final FileHandle texture = Gdx.files.internal("map/" + "Farmhouse" + ".png/");

    public Farmhouse() {
        super("Farmhouse", new Vector2(-300, 0));

    }

    @Override
    public void load() {
        img = new Sprite(new Texture(texture));
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {new Vector2(-2200, -450),
                new Vector2(-1000, -1500),
                new Vector2(650, -1500),
                new Vector2(1750, 320),
                new Vector2(0, 1600)};
        return spawnPoints;
    }
}
