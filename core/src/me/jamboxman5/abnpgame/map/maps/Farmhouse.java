package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Farmhouse extends Map {

    private static Texture texture;

    public Farmhouse() {
        super("Farmhouse", new Vector2(530, 300));
        type = MapType.FARMHOUSE;

    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector2[] getZombieSpawns() {
        Vector2[] spawnPoints = {
                new Vector2(0, 680),
                new Vector2(1085, 555),
                new Vector2(820, 720),
                new Vector2(515, 720),
                new Vector2(0, 500)
        };
        return spawnPoints;
    }

    public static void setTexture(Texture texture) {
        Farmhouse.texture = texture;
    }
}
