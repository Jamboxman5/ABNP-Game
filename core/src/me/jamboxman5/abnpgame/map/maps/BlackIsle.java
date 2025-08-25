package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

import java.io.File;

public class BlackIsle extends Map {

    private static Texture texture;

    public BlackIsle() {

        super("Black Isle", new Vector3(2200, 0, 1280));
        type = MapType.BLACKISLE;
    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector3[] getZombieSpawns() {
        Vector3[] spawnPoints = {
                new Vector3(1768, 0, 1219),
                new Vector3(1297, 0, 1090),
                new Vector3(565, 0, 967),
                new Vector3(933, 0, 0),
                new Vector3(230, 0, 1866),
                new Vector3(1540, 0, 2360),
                new Vector3(2860, 0, 2000),
                new Vector3(2833, 0, 380),
                new Vector3(0, 0, 701)
        };
        return spawnPoints;
    }

    public static void setTexture(Texture texture) {
        BlackIsle.texture = texture;
    }

}
