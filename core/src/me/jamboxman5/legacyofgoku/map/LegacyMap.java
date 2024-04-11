package me.jamboxman5.legacyofgoku.map;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class LegacyMap {

    private Sprite img;
    private MapLevel name;

    public Sprite getImage() {
        return img;
    }
    public void setImage(Sprite image) { img = image; }
    public String toString() { return name.toString(); }

    public String getName() { return name.toString(); }

    public LegacyMap(MapLevel level, boolean respawnEntitiies, int x, int y) {

    }

    public enum MapLevel {
        STARTMAP, DUNGEON1, DUNGEON2;
    }

}
