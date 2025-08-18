package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Karnivale extends Map {

    private static Texture texture;

    public Karnivale() {
        super("Karnivale", new Vector2(560, 650));
        type = MapType.KARNIVALE;
    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {
                new Vector2(180, 0),
                new Vector2(850, 0),
                new Vector2(1090, 470),
                new Vector2(1090, 820),
                new Vector2(190, 1090),
                new Vector2(0, 690),
                new Vector2(0, 300),
                new Vector2(700, 1090)
        };
        return spawnPoints;
    }

    public static void setTexture(Texture texture) {
        Karnivale.texture = texture;
    }
}
