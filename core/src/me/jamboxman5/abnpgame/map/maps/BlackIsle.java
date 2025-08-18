package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

import java.io.File;

public class BlackIsle extends Map {

    private static Texture texture;

    public BlackIsle() {

        super("Black Isle", new Vector2(2200, 1280));
        type = MapType.BLACKISLE;
    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {
                new Vector2(1768, 1219),
                new Vector2(1297, 1090),
                new Vector2(565, 967),
                new Vector2(933, 0),
                new Vector2(230, 1866),
                new Vector2(1540, 2360),
                new Vector2(2860, 2000),
                new Vector2(2833, 380),
                new Vector2(0, 701)};
        return spawnPoints;
    }
    public static void setTexture(Texture texture) {
        BlackIsle.texture = texture;
    }

}
