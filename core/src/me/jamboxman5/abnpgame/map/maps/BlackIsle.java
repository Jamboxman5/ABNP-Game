package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;

import java.io.File;

public class BlackIsle extends Map {

    private static final FileHandle texture = (Gdx.files.internal("map/" + "Black_Isle" + ".png/"));

    public BlackIsle() {
        super("Black Isle", new Vector2(3040, 1800));
    }

    @Override
    public void load() {
        img = new Sprite(new Texture(texture));
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {new Vector2(2165, 1675),
                new Vector2(1230, 1400),
                new Vector2(-240, 1175),
                new Vector2(-4030, -360),
                new Vector2(4150, 3800)};
        return spawnPoints;
    }
}
