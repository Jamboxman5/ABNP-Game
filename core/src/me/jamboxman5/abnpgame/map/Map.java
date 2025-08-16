package me.jamboxman5.abnpgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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

	public static void loadMaps(AssetManager assets) {
		Airbase.setTexture(assets.get("map/" + "Airbase" + ".png/", Texture.class));
		BlackIsle.setTexture(assets.get("map/" + "Black_Isle" + ".png/", Texture.class));
		Farmhouse.setTexture(assets.get("map/" + "Farmhouse" + ".png/", Texture.class));
		Karnivale.setTexture(assets.get("map/" + "Karnivale" + ".png/", Texture.class));
		Verdammtenstadt.setTexture(assets.get("map/" + "Verdammtenstadt" + ".png/", Texture.class));
	}

	public static void loadAssets(AssetManager assets) {
		assets.load("map/" + "Airbase" + ".png/", Texture.class);
		assets.load("map/" + "Black_Isle" + ".png/", Texture.class);
		assets.load("map/" + "Farmhouse" + ".png/", Texture.class);
		assets.load("map/" + "Karnivale" + ".png/", Texture.class);
		assets.load("map/" + "Verdammtenstadt" + ".png/", Texture.class);
	}
}
