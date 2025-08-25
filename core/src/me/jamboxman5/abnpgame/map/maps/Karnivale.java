package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Karnivale extends Map {

    private static Texture texture;

    public Karnivale() {
        super("Karnivale", new Vector3(560, 0, 650));
        type = MapType.KARNIVALE;
    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector3[] getZombieSpawns() {
        Vector3[] spawnPoints = {
                new Vector3(180, 0, 0),
                new Vector3(850, 0, 0),
                new Vector3(1090, 0, 470),
                new Vector3(1090, 0, 820),
                new Vector3(190, 0, 1090),
                new Vector3(0, 0, 690),
                new Vector3(0, 0, 300),
                new Vector3(700, 0, 1090)
        };
        return spawnPoints;
    }


    public static void setTexture(Texture texture) {
        Karnivale.texture = texture;
    }
}
