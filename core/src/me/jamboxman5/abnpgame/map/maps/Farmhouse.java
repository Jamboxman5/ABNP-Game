package me.jamboxman5.abnpgame.map.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.MapType;

public class Farmhouse extends Map {

    private static Texture texture;

    public Farmhouse() {
        super("Farmhouse", new Vector3(530, 0, 300));
        type = MapType.FARMHOUSE;

    }

    @Override
    public void load() {
        img = new Sprite(texture);
        img.scale(1.4f);
    }

    @Override
    public Vector3[] getZombieSpawns() {
        Vector3[] spawnPoints = {
                new Vector3(0, 0, 680),
                new Vector3(1085, 0, 555),
                new Vector3(820, 0, 720),
                new Vector3(515, 0, 720),
                new Vector3(0, 0, 500)
        };
        return spawnPoints;
    }


    public static void setTexture(Texture texture) {
        Farmhouse.texture = texture;
    }
}
