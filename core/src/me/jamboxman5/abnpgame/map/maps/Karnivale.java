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
        super("Karnivale", new Vector2(-315, 850));
        type = MapType.KARNIVALE;
    }

    @Override
    public void load() {
        img = new Sprite(texture);
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

    public static void setTexture(Texture texture) {
        Karnivale.texture = texture;
    }
}
