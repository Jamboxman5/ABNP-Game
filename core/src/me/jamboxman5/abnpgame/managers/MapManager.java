package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.entity.ally.Ally;
import me.jamboxman5.abnpgame.entity.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.projectile.Projectile;
import me.jamboxman5.abnpgame.entity.zombie.Zombie;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.weapon.Weapon;

import java.awt.*;
import java.util.HashMap;

public class MapManager {
	
	private final ABNPGame game;

	public Array<Entity> survivors = new Array<>();
	public Array<Entity> disposingSurvivors = new Array<>();
	public Array<Entity> entities = new Array<>();
	public Array<Entity> disposingEntities = new Array<>();
	public Array<Projectile> projectiles = new Array<>();
	public Array<Projectile> disposingProjectiles = new Array<>();
	public Array<Sprite> splatters = new Array<>();
	public Array<Map> maps = new Array<>();

	public HashMap<Sprite, Vector3> splatterLocs = new HashMap<>();

	int splatterTimer = 0;

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

	public void draw(SpriteBatch batch, ShapeRenderer renderer) {
		
//		if (game.getScreen().getClass() != GameScreen.class) return;
//
        int screenX = (int) (0 - game.getPlayer().getWorldX());
        int screenY = (int) (0 - game.getPlayer().getWorldY());

//		int screenX = 0;
//		int screenY = 0;

		batch.begin();
		m.getImage().setScale(.5f);
		m.getImage().setOrigin(screenX, screenY);
		m.getImage().draw(batch);

		batch.end();
		drawSplatters(game.batch);
		drawProjectiles(renderer);
		drawEntities();
	}
	
	public void updateEntities() {
		for (Entity e : entities) { e.update(); }
		for (Entity e : disposingEntities) {
			if (entities.contains(e, false)) entities.removeValue(e, false);
		}
		disposingEntities = new Array<>();
		for (Entity e : survivors) { e.update(); }
		for (Entity e : disposingSurvivors) {
			if (survivors.contains(e, false)) survivors.removeValue(e, false);
		}
		disposingSurvivors = new Array<>();
	}

	public Zombie getNearestZombie(Vector2 from) {
		Vector2 searchLoc = from.cpy();
		Zombie closest = null;
		for (int i = 0; i < entities.size; i++) {
			Entity e = entities.get(i);
			if (e instanceof Zombie) {
				if (closest == null) {
					closest = (Zombie) e;
				} else {
					if (e.distanceTo(searchLoc) < closest.distanceTo(searchLoc)) closest = (Zombie) e;
				}
			}
		}
		return closest;
	}
	
	public void drawEntities() {
		for (Entity e : entities) {
			e.draw(game.batch, game.shapeRenderer);
			if (game.debugMode) {
				e.drawCollision(game.shapeRenderer);
			}
		}
		for (Entity e : survivors) {
			e.draw(game.batch, game.shapeRenderer);
			if (game.debugMode) {
				e.drawCollision(game.shapeRenderer);
			}
		}

	}
	
	public void updateProjectiles() {
		for (Projectile p : projectiles) { p.update(); }
		for (Projectile p : disposingProjectiles) {
			if (projectiles.contains(p, false)) projectiles.removeValue(p, false);
		}
		disposingEntities = new Array<>();
	}
	
	public void drawProjectiles(ShapeRenderer renderer) {
		for (Projectile p : projectiles) { p.draw(renderer); }
	}
	
	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}
	public void drawSplatters(SpriteBatch batch) {
		if (splatters.size == 0) return;
		splatterTimer++;


		batch.begin();

		for (int i = 0; i < splatters.size; i++) {
			Vector3 position = splatterLocs.get(splatters.get(i));

			int x = (int) (((position.x - game.getPlayer().getWorldX())*.5) + game.getPlayer().getScreenX());
			int y = (int) (((position.y - game.getPlayer().getWorldY())*.5) + game.getPlayer().getScreenY());
			float opacity = position.z;

			splatters.get(i).setAlpha(opacity);
			splatters.get(i).setCenter(x, y);
			splatters.get(i).draw(batch);

			if (position.z > 0) position.z -= .002;

		}
		if (splatterTimer > 600) {
			splatterLocs.remove(splatters.get(splatters.size-1));
			splatters.removeIndex(splatters.size-1);
			splatterTimer = 0;
		}

		batch.end();
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

	public void clearMap() {
		entities = new Array<>();
		projectiles = new Array<>();
		survivors = new Array<>();
	}

	public void addSplatter(Vector2 position) {
		Sprite splatter = new Sprite(Zombie.deadSprite);
		splatter.setCenter(position.x, position.y);
		splatter.setRotation((float) (Math.random() * 360));
		splatters.insert(0, splatter);
		splatterLocs.put(splatter, new Vector3(position.x, position.y, 1f));
	}

	public void addAlly(Ally sarge) { survivors.add(sarge);}
}
