package me.jamboxman5.abnpgame.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Map {

	protected Sprite img;
	protected String name;
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
	
}
