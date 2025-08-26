package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.entity.mob.npc.Ally;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.entity.projectile.Projectile;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.prop.pickup.Pickup;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.maps.Verdammtenstadt;
import me.jamboxman5.abnpgame.net.packets.PacketDisconnect;
import me.jamboxman5.abnpgame.net.packets.PacketMove;
import me.jamboxman5.abnpgame.net.packets.PacketShoot;
import me.jamboxman5.abnpgame.net.packets.PacketWeaponChange;

import java.util.HashMap;

public class MapManager {
	
	private final ABNPGame game;

	public Array<Entity> survivors = new Array<>();
	public Array<Entity> disposingSurvivors = new Array<>();
	public Array<Entity> entities = new Array<>();
	public Array<Entity> disposingEntities = new Array<>();
	public Array<Projectile> projectiles = new Array<>();
	public Array<Projectile> disposingProjectiles = new Array<>();
	public Array<Decal> splatters = new Array<>();
	public Array<Map> maps = new Array<>();

	public HashMap<Decal, Vector3> splatterLocs = new HashMap<>();

	int splatterTimer = 0;

	Map activeMap;


	//3d

	private Model groundModel;
	private Model boxModel1;
	private Model boxModel2;
	private Model boxModel3;
	private Model boxModel4;
	private Model boxModel5;
	private ModelInstance groundInstance;
	private ModelInstance boxInstance1;
	private ModelInstance boxInstance2;
	private ModelInstance boxInstance3;
	private ModelInstance boxInstance4;
	private ModelInstance boxInstance5;

	public MapManager(ABNPGame game) {
		this.game = game;
		activeMap = new Verdammtenstadt();

		float height1 = (float) (Math.random() * 500f);
		float height2 = (float) (Math.random() * 500f);
		float height3 = (float) (Math.random() * 500f);
		float height4 = (float) (Math.random() * 500f);
		float height5 = (float) (Math.random() * 500f);


		//3d 'assets'
		ModelBuilder modelBuilder = new ModelBuilder();
		groundModel = modelBuilder.createBox(10000f, 1f, 10000f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		groundInstance = new ModelInstance(groundModel);
		groundInstance.transform.setToTranslation(5000f, 0, -5000f);

		boxModel1 = modelBuilder.createBox(200f, height1, 100f,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		boxModel2 = modelBuilder.createBox(200f, height2, 200f,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		boxModel3 = modelBuilder.createBox(400f, height3, 400f,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		boxModel4 = modelBuilder.createBox(200f, height4, 100f,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		boxModel5 = modelBuilder.createBox(200f, height5, 100f,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		boxInstance1 = new ModelInstance(boxModel1, (float) Math.random() * 1000f, height1/2f, -(float) Math.random() * 1000f);
		boxInstance2 = new ModelInstance(boxModel2, (float) Math.random() * 1000f, height2/2f, -(float) Math.random() * 1000f);
		boxInstance3 = new ModelInstance(boxModel3, (float) Math.random() * 1000f, height3/2f, -(float) Math.random() * 1000f);
		boxInstance4 = new ModelInstance(boxModel4, (float) Math.random() * 1000f, height4/2f, -(float) Math.random() * 1000f);
		boxInstance5 = new ModelInstance(boxModel5, (float) Math.random() * 1000f, height5/2f, -(float) Math.random() * 1000f);
	}

	public void draw(Environment environment, DecalBatch batch, ModelBatch modelBatch, ShapeRenderer shapes, PerspectiveCamera camera) {

		//3d test

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Vector3 playerPos = game.getPlayer().getPosition();

		camera.position.set(playerPos.x, camera.position.y, -playerPos.z);   // 50 units north of origin
		camera.lookAt(playerPos.x, 0, -playerPos.z);             // look at ground center

		camera.update();

		modelBatch.begin(camera);
		modelBatch.render(groundInstance, environment);
		modelBatch.render(boxInstance1, environment);
		modelBatch.render(boxInstance2, environment);
		modelBatch.render(boxInstance3, environment);
		modelBatch.render(boxInstance4, environment);
		modelBatch.render(boxInstance5, environment);
		modelBatch.end();

		drawSplatters(batch);
		drawProjectiles(shapes);
		drawEntities(batch, shapes, camera);
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

	public Zombie getNearestZombie(Vector3 from) {
		Vector3 searchLoc = from.cpy();
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
	
	public void drawEntities(DecalBatch batch, ShapeRenderer shapes, PerspectiveCamera camera) {
		for (Entity e : entities) {
			e.draw(batch, shapes, camera);
			if (game.debugMode) {
				e.drawCollision(shapes);
			}
		}
		for (Entity e : survivors) {
			e.draw(batch, shapes, camera);
			if (game.debugMode) {
				e.drawCollision(shapes);
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
	public void drawSplatters(DecalBatch batch) {
		if (splatters.size == 0) return;
		splatterTimer++;

		// Use camera projection instead of manual offsets

		for (int i = 0; i < splatters.size; i++) {
			Vector3 position = splatterLocs.get(splatters.get(i));
			float opacity = 1f / (splatterTimer / 600f);

			Decal splatter = splatters.get(i);
			splatter.setColor(1f, 1f, 1f, opacity);
			splatter.setPosition(position.x, 1, -position.y); // world coords directly


			batch.add(splatter);

//			if (position.z > 0) position.z -= .002f;
		}

		if (splatterTimer > 600) {
			splatterLocs.remove(splatters.get(splatters.size - 1));
			splatters.removeIndex(splatters.size - 1);
			splatterTimer = 0;
		}

	}


	public void disposeProjectile(Projectile p) {
		disposingProjectiles.add(p);
	}

	public Map getActiveMap() {	return activeMap; }
	
	public void addEntity(Entity entity) { entities.add(entity); }

	public Array<Entity> getEntities() { return entities; }

	public void setMap(Map newMap) {
		if (newMap != null) newMap.load();
		activeMap = newMap;
	}

	public void setMap(String map) {
		for(Map m2 : maps) {
			if (m2.toString().equals(map)) {
				activeMap = m2;
				game.getPlayer().setWorldX(activeMap.getPlayerSpawn().x);
				game.getPlayer().setWorldZ(activeMap.getPlayerSpawn().y);
				return;
			}
		}
	}
	
	public Array<Map> getMapList() { return maps; }

	public void clearMap() {
		entities = new Array<>();
		projectiles = new Array<>();
		survivors = new Array<>();
	}

	public void addSplatter(Vector3 position) {
		Decal splatter = Decal.newDecal(new TextureRegion(Zombie.deadSprite.getTexture()), true);
		splatter.setRotationZ((float) (Math.random() * 360));
		splatters.insert(0, splatter);
		splatter.lookAt(splatter.getPosition().cpy().add(0f, 1f, 0f), new Vector3((float) (Math.random() * 90), 0f, (float) (Math.random() * 90)));
		splatterLocs.put(splatter, position.cpy());
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

	public Array<Zombie> getZombies() {
		Array<Zombie> zombs = new Array<>();
		for (Entity e : entities) {
			if (e instanceof Zombie) zombs.add((Zombie) e);
		}
		return zombs;
	}

	public Array<Survivor> getSurvivors() {
		Array<Survivor> s = new Array<>();
		for (Entity e : survivors) {
			if (e != game.getPlayer()) s.add((Survivor) e);
		}
		return s;
	}

	public Array<Pickup> getPickups() {
		Array<Pickup> s = new Array<>();
		for (Entity e : entities) {
			if (e instanceof Pickup) s.add((Pickup) e);
		}
		return s;
	}
}
