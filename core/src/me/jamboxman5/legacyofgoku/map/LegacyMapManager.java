package me.jamboxman5.legacyofgoku.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.legacyofgoku.LegacyGame;
import me.jamboxman5.legacyofgoku.entity.LegacyEntity;

import java.util.HashMap;

public class LegacyMapManager {

    private final LegacyGame game;

    private static LegacyMap activeMap;
    private static Array<LegacyEntity> activeEntities;
    private World world;
    private ShapeRenderer shapeRenderer;
    public HashMap<LegacyMap.MapLevel, LegacyMap> maps = new HashMap<>();

    public LegacyMapManager(LegacyGame game) {
        this.game = game;
        world = new World(new Vector2(0,0), true);
        shapeRenderer = new ShapeRenderer();
        getMaps();
    }

    public void setActiveMap(LegacyMap.MapLevel level) { activeMap = maps.get(level); }
    public static void setActiveMap(LegacyMap map) { activeMap = map; }

    public void draw(SpriteBatch batch) {

//		if (game.getScreen().getClass() != GameScreen.class) return;
//
//        int screenX = (int) (0 - game.getPlayer().getAdjustedWorldX() + game.getPlayer().getScreenX());
//        int screenY = (int) (0 - game.getPlayer().getAdjustedWorldY() + game.getPlayer().getScreenY());

		int screenX = 0;
		int screenY = 0;
        activeMap.getImage().setScale(0);
        batch.draw(activeMap.getImage(), screenX,screenY);
    }
    public static void setEntities(Array<LegacyEntity> entities) {
        activeEntities = entities;
    }
    public static void teleport(LegacyGateway gate) {
        gate.teleport();
    }

    public static LegacyMap getActiveMap() { return activeMap; }


    void getMaps() {
        setup(LegacyMap.MapLevel.STARTMAP);
        setup(LegacyMap.MapLevel.DUNGEON1);
        setup(LegacyMap.MapLevel.DUNGEON2);
    }



    public void setup(LegacyMap.MapLevel level) {
        LegacyMap map;
        map = new LegacyMap(level, true, 150, 150);
        Texture texture = new Texture(Gdx.files.internal("demos/legacy/map/" + level.toString() + ".png/"));
        Sprite sprite = new Sprite(texture);
        sprite.scale(1.4f);
        map.setImage(sprite);
        maps.put(level, map);
    }

}
