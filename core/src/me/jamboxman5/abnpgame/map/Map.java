package me.jamboxman5.abnpgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.map.maps.*;

public abstract class Map {

	protected Sprite img;
	protected String name;
	protected MapType type;
	protected final Vector2 spawnPosition;
	
	public Map(String name, Vector2 start) {
		this.name = name;
		spawnPosition = start;
	}
	
	public Sprite getImage() { return img; }
	public String toString() { return name; }
	public Vector2 getPlayerSpawn() { return spawnPosition; }

	public String getName() { return name; }

	public abstract Vector2[] getZombieSpawns();
	public abstract void load();

	public MapType getMapType() { return type; }

	public static void loadMaps() {
		Airbase.setTexture(new Texture(Gdx.files.internal("map/" + "Airbase" + ".png/")));
		BlackIsle.setTexture(new Texture(Gdx.files.internal("map/" + "Black_Isle" + ".png/")));
		Farmhouse.setTexture(new Texture(Gdx.files.internal("map/" + "Farmhouse" + ".png/")));
		Karnivale.setTexture(new Texture(Gdx.files.internal("map/" + "Karnivale" + ".png/")));
		Verdammtenstadt.setTexture(new Texture(Gdx.files.internal("map/" + "Verdammtenstadt" + ".png/")));
	}
}
