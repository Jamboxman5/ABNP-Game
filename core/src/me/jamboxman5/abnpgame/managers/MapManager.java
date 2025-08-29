package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
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
	public Array<Splatter> splatters = new Array<>();
	public Array<Map> maps = new Array<>();
	public Array<Building> buildings = new Array<>();

	int splatterTimer = 0;

	Map activeMap;


	//3d

	private Model groundModel;

	private ModelInstance groundInstance;


	public MapManager(ABNPGame game) {
		this.game = game;
		activeMap = new Verdammtenstadt();

		//3d 'assets'
		ModelBuilder modelBuilder = new ModelBuilder();
		// Create the box as before
		groundModel = modelBuilder.createBox(
				10000f, 1f, 10000f,
				new Material(),
				VertexAttributes.Usage.Position |
						VertexAttributes.Usage.Normal |
						VertexAttributes.Usage.TextureCoordinates
		);
		groundInstance = new ModelInstance(groundModel);
		groundInstance.transform.setToTranslation(5000f, 0, -5000f);

// Load the texture
		Texture grass = new Texture(Gdx.files.internal("map/environment/grass.png"));
		grass.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

// Apply the texture to the material
		for (Material mat : groundInstance.materials) {
			mat.set(TextureAttribute.createDiffuse(grass));
			mat.set(ColorAttribute.createDiffuse(Color.WHITE));
		}

// Modify top face UVs automatically
		Mesh mesh = groundInstance.model.meshParts.get(0).mesh;
		float[] vertices = new float[mesh.getNumVertices() * mesh.getVertexSize() / 4]; // float size
		mesh.getVertices(vertices);

		int vertexSize = mesh.getVertexSize() / 4; // floats per vertex
		int posOffset = mesh.getVertexAttribute(VertexAttributes.Usage.Position).offset / 4;
		int uvOffset  = mesh.getVertexAttribute(VertexAttributes.Usage.TextureCoordinates).offset / 4;

		float repeatX = 50f; // how many times to repeat across X
		float repeatZ = 50f; // how many times to repeat across Z

// 1. Find the max Y (top face)
		float maxY = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < mesh.getNumVertices(); i++) {
			int offset = i * vertexSize + posOffset;
			float y = vertices[offset + 1];
			if (y > maxY) maxY = y;
		}

// 2. Update UVs for all vertices at max Y
		for (int i = 0; i < mesh.getNumVertices(); i++) {
			int offset = i * vertexSize;
			float y = vertices[offset + posOffset + 1];
			if (Math.abs(y - maxY) < 0.001f) { // consider it top face
				float x = vertices[offset + posOffset];
				float z = vertices[offset + posOffset + 2];

				vertices[offset + uvOffset]     = (x / 10000f) * repeatX; // U
				vertices[offset + uvOffset + 1] = (z / 10000f) * repeatZ; // V
			}
		}

		mesh.setVertices(vertices);


		G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());

		Model model = loader.loadModel(Gdx.files.internal("map/structure/cottage.g3db"));

		Texture diffuse = new Texture(Gdx.files.internal("map/structure/cottage_diffuse.png"));

		Texture normal = new Texture(Gdx.files.internal("map/structure/cottage_normal.png"));
		normal.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		for (Material mat : model.materials) {
			mat.set(TextureAttribute.createDiffuse(diffuse));
			mat.set(TextureAttribute.createNormal(normal));
			mat.set(ColorAttribute.createDiffuse(Color.WHITE));
		}


		buildings.add(new Building(new Vector3((float) (Math.random() * 2000f), 0, -(float) (Math.random()*2000f)), model, (float) (Math.random() * 90f), .2f));
		buildings.add(new Building(new Vector3((float) (Math.random() * 2000f), 0, -(float) (Math.random()*2000f)), model, (float) (Math.random() * 90f), .2f));
		buildings.add(new Building(new Vector3((float) (Math.random() * 2000f), 0, -(float) (Math.random()*2000f)), model, (float) (Math.random() * 90f), .2f));
		buildings.add(new Building(new Vector3((float) (Math.random() * 2000f), 0, -(float) (Math.random()*2000f)), model, (float) (Math.random() * 90f), .2f));
		buildings.add(new Building(new Vector3((float) (Math.random() * 2000f), 0, -(float) (Math.random()*2000f)), model, (float) (Math.random() * 90f), .2f));

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
		for (Building b : buildings) {
			modelBatch.render(b.instance, environment);
		}
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

			Splatter splatter = splatters.get(i);
			splatter.decal.setColor(1f, 1f, 1f, splatter.alpha);
			splatter.decal.setPosition(splatter.position.x, 1, -splatter.position.z); // world coords directly


			batch.add(splatter.decal);

			if (splatter.alpha > 0) splatter.alpha -= .002f;
		}

		if (splatterTimer > 600) {
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
		splatter.setScale(.25f);
		splatters.insert(0, new Splatter(splatter, position));
		splatter.lookAt(splatter.getPosition().cpy().add(0f, 1f, 0f), new Vector3((float) (Math.random() * 90), 0f, (float) (Math.random() * 90)));
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

	private static class Splatter {

		float alpha;
		Decal decal;
		Vector3 position;
		public Splatter(Decal decal, Vector3 position) {
			this.decal = decal;
			this.position = position;
			this.alpha = 1f;
		}
	}

	private static class Building {

		Model model;
		ModelInstance instance;
		float rotation;
		Vector3 position;

		public Building(Vector3 position, Model model, float rotation, float scale) {
			this.model = model;
			this.rotation = rotation;
			this.position = position;

			instance = new ModelInstance(this.model, position);
			instance.transform.scl(scale);
			instance.transform.rotate(Vector3.Y, rotation);

		}

	}
}
