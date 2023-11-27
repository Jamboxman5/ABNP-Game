package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.entity.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.projectile.Projectile;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.weapon.Weapon;

import java.awt.*;

public class MapManager {
	
	private final ABNPGame game;
	
	public Array<Entity> entities = new Array<>();
	public Array<Entity> disposingEntities = new Array<>();
	public Array<Projectile> projectiles = new Array<>();
	public Array<Projectile> disposingProjectiles = new Array<>();
	public Array<Map> maps = new Array<>();

	MapRenderer renderer;
	
	Map m;
	private World world;
	private Box2DDebugRenderer dbgRenderer;
	
	public MapManager(ABNPGame game) {
		this.game = game;
		world = new World(new Vector2(0,0), true);
		dbgRenderer = new Box2DDebugRenderer();
		getMaps();
	}

	void getMaps() {
		setup("Verdammtenstadt");
		setup("Black_Isle");
		setup("Farmhouse");
		setup("Karnivale");
		setup("Airbase");
	}

	public void draw(SpriteBatch batch) {
		
//		if (game.getScreen().getClass() != GameScreen.class) return;
//
        int screenX = (int) (0 - game.getPlayer().getAdjustedWorldX() + game.getPlayer().getScreenX());
        int screenY = (int) (0 - game.getPlayer().getAdjustedWorldY() + game.getPlayer().getScreenY());

//		int screenX = 0;
//		int screenY = 0;
		m.getImage().setScale(0);
		batch.draw(m.getImage(), screenX,screenY);
	}
	
	public void updateEntities() {
		for (Entity e : entities) { e.update(); }
		for (Entity e : disposingEntities) {
			if (entities.contains(e, false)) entities.removeValue(e, false);
		}
		disposingEntities = new Array<>();
	}
	
	public void drawEntities(Graphics2D g2) {
		for (Entity e : entities) { e.draw(game.batch); }
	}
	
	public void updateProjectiles() {
		for (Projectile p : projectiles) { p.update(); }
		for (Projectile p : disposingProjectiles) {
			if (projectiles.contains(p, false)) projectiles.removeValue(p, false);
		}
		disposingEntities = new Array<>();
	}
	
	public void drawProjectiles(Graphics2D g2) {
		for (Projectile p : projectiles) { p.draw(g2); }
	}
	
	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}
	
	public void disposeProjectile(Projectile p) {
		disposingProjectiles.add(p);
	}
	
	public void setup(String imageName) {
		int x = 0;
		int y = 0;
		if (imageName.equals("Black_Isle")) {
			x = 1753;
			y = 1232;
		} else if (imageName.equals("Verdammtenstadt")) {
			x = 1426;
			y = 1374;
		} else if (imageName.equals("Farmhouse")) {
			x = 583;
			y = 483;
		} else if (imageName.equals("Airbase")) {
			x = 583;
			y = 483;
		} else if (imageName.equals("Karnivale")) {
			x = 583;
			y = 483;
		}
		m = new Map(imageName, x, y);
		Texture texture = new Texture(Gdx.files.internal("map/" + imageName + ".png/"));
		Sprite sprite = new Sprite(texture);
		sprite.scale(1.4f);
		m.setImage(sprite);
		maps.add(m);
	}

	public Map getActiveMap() {	return m; }
	
	public void addEntity(Entity entity) { entities.add(entity); }

	public Array<Entity> getEntities() { return entities; }

	public void removeConnectedPlayer(String username) {
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i) instanceof OnlinePlayer &&
				((OnlinePlayer)entities.get(i)).getUsername().equals(username)) {
				entities.removeIndex(i);
				break;
			}
		}
	}
	
	private int getConnectedPlayerIndex(String username) {
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i) instanceof OnlinePlayer && 
				((OnlinePlayer)entities.get(i)).getUsername().equals(username)) {
				return i;
			}
		}
		return -1;
	}
	
	public void movePlayer(String username, double x, double y, double rotation) {
		int index = getConnectedPlayerIndex(username);
		if (index < 0) return;
		entities.get(index).setWorldX(x);
		entities.get(index).setWorldY(y);
		entities.get(index).setRotation(rotation);
	}

	public void setMap(int mapIndex) {
		m = maps.get(mapIndex);
	}

	public void setMap(String map) {
		for(Map m2 : maps) {
			if (m2.toString().equals(map)) {
				m = m2;
				game.getPlayer().setWorldX(m.getDefaultX());
				game.getPlayer().setWorldY(m.getDefaultY());
				return;
			}
		}
	}
	
	public Array<Map> getMapList() { return maps; }

	public void setWeapon(String username, Weapon weapon) {
		int idx = getConnectedPlayerIndex(username);
		if (idx < 0) return;
		Weapon oldWeapon = ((OnlinePlayer)entities.get(idx)).getWeaponLoadout().getActiveWeapon();
		((OnlinePlayer)entities.get(idx)).getWeaponLoadout().addWeapon(weapon, true);
		((OnlinePlayer)entities.get(idx)).getWeaponLoadout().removeWeapon(oldWeapon);
	}

	public World getWorld() { return world; }

	public Box2DDebugRenderer getDebugRenderer() { return dbgRenderer; }
}
