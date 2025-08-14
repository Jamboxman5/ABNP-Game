package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.entity.mob.npc.Ally;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.entity.projectile.Projectile;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.maps.Verdammtenstadt;
import me.jamboxman5.abnpgame.net.packets.PacketDisconnect;
import me.jamboxman5.abnpgame.net.packets.PacketMove;
import me.jamboxman5.abnpgame.net.packets.PacketShoot;
import me.jamboxman5.abnpgame.net.packets.PacketWeaponChange;
import me.jamboxman5.abnpgame.weapon.Weapon;

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

	Map activeMap;
	
	public MapManager(ABNPGame game) {
		this.game = game;
		activeMap = new Verdammtenstadt();
	}

	public void draw(SpriteBatch batch, ShapeRenderer renderer, Camera camera) {
		
//		if (game.getScreen().getClass() != GameScreen.class) return;
//
        int screenX = (int) (0 - game.getPlayer().getWorldX());
        int screenY = (int) (0 - game.getPlayer().getWorldY());

//		int screenX = 0;
//		int screenY = 0;

		batch.begin();
		activeMap.getImage().setScale(.5f);
		activeMap.getImage().setOrigin(screenX, screenY);
		activeMap.getImage().draw(batch);

		batch.end();
		drawSplatters(game.canvas);
		drawProjectiles(renderer);
		drawEntities();
	}
	
	public void updateEntities(float delta) {
		for (Entity e : entities) { e.update(delta); }
		for (Entity e : disposingEntities) {
			if (entities.contains(e, false)) entities.removeValue(e, false);
		}
		disposingEntities = new Array<>();
		for (Entity e : survivors) { e.update(delta); }
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
			e.draw(game.canvas, game.shapeRenderer);
			if (game.debugMode) {
				e.drawCollision(game.shapeRenderer);
			}
		}
		for (Entity e : survivors) {
			e.draw(game.canvas, game.shapeRenderer);
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

			if (position.z > 0) position.z -= .002f;

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

	public Map getActiveMap() {	return activeMap; }
	
	public void addEntity(Entity entity) { entities.add(entity); }

	public Array<Entity> getEntities() { return entities; }

//	public void removeConnectedPlayer(String username) {
//		for (int i = 0; i < entities.size; i++) {
//			if (entities.get(i) instanceof OnlinePlayer &&
//				((OnlinePlayer)entities.get(i)).getUsername().equals(username)) {
//				entities.removeIndex(i);
//				break;
//			}
//		}
//	}
//
//	private int getConnectedPlayerIndex(String username) {
//		for (int i = 0; i < entities.size; i++) {
//			if (entities.get(i) instanceof OnlinePlayer &&
//				((OnlinePlayer)entities.get(i)).getUsername().equals(username)) {
//				return i;
//			}
//		}
//		return -1;
//	}
//
//	public void movePlayer(String username, double x, double y, float rotation) {
//		int index = getConnectedPlayerIndex(username);
//		if (index < 0) return;
//		entities.get(index).setWorldX(x);
//		entities.get(index).setWorldY(y);
//		entities.get(index).setRotation(rotation);
//	}

	public void setMap(Map newMap) {
		if (newMap != null) newMap.load();
		activeMap = newMap;
	}

	public void setMap(String map) {
		for(Map m2 : maps) {
			if (m2.toString().equals(map)) {
				activeMap = m2;
				game.getPlayer().setWorldX(activeMap.getPlayerSpawn().x);
				game.getPlayer().setWorldY(activeMap.getPlayerSpawn().y);
				return;
			}
		}
	}
	
	public Array<Map> getMapList() { return maps; }

//	public void setWeapon(String username, Weapon weapon) {
//		int idx = getConnectedPlayerIndex(username);
//		if (idx < 0) return;
//		Weapon oldWeapon = ((OnlinePlayer)entities.get(idx)).getWeaponLoadout().getActiveWeapon();
//		((OnlinePlayer)entities.get(idx)).getWeaponLoadout().addWeapon(weapon, true);
//		((OnlinePlayer)entities.get(idx)).getWeaponLoadout().removeWeapon(oldWeapon);
//	}

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

	public void addOnlinePlayer(OnlinePlayer joining) {
		survivors.add(joining);
	}

	public void updateOnlinePlayerPosition(PacketMove packet) {
		OnlinePlayer p = findOnlinePlayer(packet.uuid);
		if (p != null) {
			p.updatePos(packet);
		}
	}

	public void updateOnlinePlayerWeapon(PacketWeaponChange packet) {
		OnlinePlayer p = findOnlinePlayer(packet.uuid);
		if (p != null) {
			p.updateWeapon(packet);
		}
	}

	public void onlinePlayerShoot(PacketShoot packet) {
		OnlinePlayer p = findOnlinePlayer(packet.uuid);
		if (p != null) {
			p.shoot();
		}
	}

	private OnlinePlayer findOnlinePlayer(String uuid) {
		for (Entity s : survivors) {
			if (s instanceof OnlinePlayer) {
				OnlinePlayer player = (OnlinePlayer) s;
				if (uuid.equals(player.getID())) {
					return player;
				}
			}
		}
		return null;
	}

	public void removeOnlinePlayer(PacketDisconnect disconnect) {
		OnlinePlayer p = findOnlinePlayer(disconnect.uuid);
		disposingSurvivors.add(p);
	}

	public void removeOnlinePlayers() {
		for (Entity s : survivors) {
			if (s instanceof OnlinePlayer) {
				disposingSurvivors.add(s);
			}
		}
	}
}
