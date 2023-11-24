package me.jamboxman5.abnpgame.map;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Map {

	private Sprite img;
	private String name;
	private final int defaultX, defaultY;
	
	public Map(String name, int x, int y) {
		this.name = name;
		defaultX = x;
		defaultY = y;
	}
	
	public Sprite getImage() {
		return img;
	}
	public void setImage(Sprite image) { img = image; }
	public String toString() { return name; }

	public int getDefaultX() { return defaultX; }
	public int getDefaultY() { return defaultY; }

	public String getName() { return name; }
	
}
