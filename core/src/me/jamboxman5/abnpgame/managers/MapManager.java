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
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.OrientedBoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import jdk.javadoc.internal.doclint.Env;
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

	public Array<Entity> entities = new Array<>();
	public Array<Entity> disposingEntities = new Array<>();

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

// Parameters
		float width = 10000f;
		float height = 1f;
		float depth = 10000f;
		int subdivisions = 100; // number of quads along each axis
		float stepX = width / subdivisions;
		float stepZ = depth / subdivisions;

// Build the mesh with tangents for future normal maps
		modelBuilder.begin();

		MeshPartBuilder builder = modelBuilder.part(
				"ground", GL20.GL_TRIANGLES,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates | VertexAttributes.Usage.Tangent,
				new Material()
		);

// Generate quads
		for (int i = 0; i < subdivisions; i++) {
			for (int j = 0; j < subdivisions; j++) {
				float x0 = i * stepX;
				float x1 = x0 + stepX;
				float z0 = -j * stepZ;
				float z1 = z0 - stepZ;

				builder.rect(
						x0, 0, z0,
						x1, 0, z0,
						x1, 0, z1,
						x0, 0, z1,
						0, 1, 0 // normal pointing up
				);
			}
		}

		groundModel = modelBuilder.end();
		groundInstance = new ModelInstance(groundModel);
		groundInstance.transform.setToTranslation(0, 0, 0);

// Load grass texture
		Texture grass = new Texture(Gdx.files.internal("map/environment/grass.png"));
		grass.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

// Apply texture to all materials
		for (Material mat : groundInstance.materials) {
			mat.set(TextureAttribute.createDiffuse(grass));
			mat.set(ColorAttribute.createDiffuse(Color.WHITE));
		}

// Modify UVs to repeat texture across the plane
		Mesh mesh = groundInstance.model.meshParts.get(0).mesh;
		float[] vertices = new float[mesh.getNumVertices() * mesh.getVertexSize() / 4];
		mesh.getVertices(vertices);

		int vertexSize = mesh.getVertexSize() / 4;
		int posOffset = mesh.getVertexAttribute(VertexAttributes.Usage.Position).offset / 4;
		int uvOffset  = mesh.getVertexAttribute(VertexAttributes.Usage.TextureCoordinates).offset / 4;

		float repeatX = 50f;
		float repeatZ = 50f;

		for (int i = 0; i < mesh.getNumVertices(); i++) {
			int offset = i * vertexSize;
			float x = vertices[offset + posOffset];
			float z = vertices[offset + posOffset + 2];

			vertices[offset + uvOffset]     = (x / width) * repeatX;
			vertices[offset + uvOffset + 1] = (Math.abs(z) / depth) * repeatZ;
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

		Vector3 playerPos = game.getPlayer().getPosition().cpy();


		Vector3 newCamPos = new Vector3(playerPos.x, camera.position.y, -playerPos.z);

		camera.position.set(newCamPos);   // 50 units north of origin
		camera.lookAt(playerPos.x, 0, -playerPos.z);             // look at ground center

		camera.update();

		modelBatch.begin(camera);
		for (Building b : buildings) {
			b.draw(modelBatch, environment);
		}

		modelBatch.render(groundInstance);
		modelBatch.end();

		drawSplatters(batch);
		drawEntities(batch, shapes, camera);
	}
	
	public void updateEntities(float delta) {
		for (Entity e : entities) { e.update(delta); }
		for (Entity e : disposingEntities) {
			if (entities.contains(e, false)) entities.removeValue(e, false);
		}
		disposingEntities = new Array<>();
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

	}

	public void addProjectile(Projectile p) {
		entities.add(p);
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
		disposingEntities.add(p);
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
	}

	public void addSplatter(Vector3 position) {
		Decal splatter = Decal.newDecal(new TextureRegion(Zombie.deadSprite.getTexture()), true);
		splatter.setRotationZ((float) (Math.random() * 360));
		splatter.setScale(.25f);
		splatters.insert(0, new Splatter(splatter, position));
		splatter.lookAt(splatter.getPosition().cpy().add(0f, 1f, 0f), new Vector3((float) (Math.random() * 90), 0f, (float) (Math.random() * 90)));
	}

	public void addAlly(Ally sarge) { entities.add(sarge);}

	public void addOnlinePlayer(OnlinePlayer joining) {
		entities.add(joining);
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
		for (Entity s : entities) {
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
		disposingEntities.add(p);
	}

	public void removeOnlinePlayers() {
		for (Entity s : entities) {
			if (s instanceof OnlinePlayer) {
				disposingEntities.add(s);
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
		for (Entity e : entities) {
			if (e instanceof Survivor) s.add((Survivor) e);
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

	public boolean collides(Vector3 position) {
		for (Entity e : entities.toArray(Entity.class)) {
			if (e.getCollision() == null) continue;
			if (e.getCollision().contains(position.x, position.z) && e.isSolid()) return true;
		}
		for (Building b : buildings) {
			Vector3 translatedPos = new Vector3(position.x, position.y, -position.z);
			if (b.getCollision().contains(translatedPos)) return true;

		}
		return false;
	}

	public Array<Projectile> getProjectiles() {
		Array<Projectile> projectiles = new Array<>();
		for (Entity e : entities) if (e instanceof Projectile) projectiles.add((Projectile) e);
		return projectiles;
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
		ModelInstance buildingModel;
		ModelInstance debugFrame;
		OrientedBoundingBox collision;

		float rotation;
		Vector3 position;

		public Building(Vector3 position, Model model, float rotation, float scale) {
			this.model = model;
			this.rotation = rotation;
			this.position = position;

			//BUILD MODEL
			buildingModel = new ModelInstance(this.model, position);
			buildingModel.transform.scl(scale);
			buildingModel.transform.rotate(Vector3.Y, rotation);

			//BUILD COLLISION BOX
			collision = new OrientedBoundingBox();
			BoundingBox bounds = new BoundingBox();
			model.calculateBoundingBox(bounds);
			collision.set(bounds, buildingModel.transform);

			Model wireModel = generateWireframe();
			debugFrame = new ModelInstance(wireModel);

			Matrix4 obbTransform = buildObbTransform(model, buildingModel.transform);
			debugFrame.transform.set(obbTransform);

		}

		public OrientedBoundingBox getCollision() { return collision; }


		public void draw(ModelBatch modelBatch, Environment environment) {

			modelBatch.render(buildingModel, environment);

			if (ABNPGame.getInstance().debugMode) {
				modelBatch.render(debugFrame);
			}

		}
	}

	public static Model generateWireframe() {
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		MeshPartBuilder b = mb.part(
				"wireframe", GL20.GL_LINES,
				VertexAttributes.Usage.Position,
				new Material(ColorAttribute.createDiffuse(Color.RED))
		);

		Vector3[] c = {
				new Vector3(-0.5f, -0.5f, -0.5f),
				new Vector3( 0.5f, -0.5f, -0.5f),
				new Vector3( 0.5f, -0.5f,  0.5f),
				new Vector3(-0.5f, -0.5f,  0.5f),
				new Vector3(-0.5f,  0.5f, -0.5f),
				new Vector3( 0.5f,  0.5f, -0.5f),
				new Vector3( 0.5f,  0.5f,  0.5f),
				new Vector3(-0.5f,  0.5f,  0.5f)
		};

		// bottom square
		b.line(c[0], c[1]); b.line(c[1], c[2]); b.line(c[2], c[3]); b.line(c[3], c[0]);
		// top square
		b.line(c[4], c[5]); b.line(c[5], c[6]); b.line(c[6], c[7]); b.line(c[7], c[4]);
		// verticals
		b.line(c[0], c[4]); b.line(c[1], c[5]); b.line(c[2], c[6]); b.line(c[3], c[7]);

		return mb.end();
	}

	public static Matrix4 buildObbTransform(Model model, Matrix4 instanceTransform) {
		BoundingBox localBounds = new BoundingBox();
		model.calculateBoundingBox(localBounds);

		Vector3 localCenter = localBounds.getCenter(new Vector3());

		// get full dimensions and then half extents
		Vector3 dims = localBounds.getDimensions(new Vector3()); // full width/height/depth
		Vector3 halfExtents = dims.scl(0.5f);

		// S = scale(halfExtents * 2)
		Matrix4 S = new Matrix4().setToScaling(halfExtents.x * 2f, halfExtents.y * 2f, halfExtents.z * 2f);

		// T = translate(localCenter)
		Matrix4 T = new Matrix4().setToTranslation(localCenter);

		// tmp = T * S  (so scaling happens first on the unit cube, then translated in model space)
		Matrix4 tmp = T.mul(S); // tmp = T * S

		// final = instanceTransform * tmp  (apply model instance transform after local offset+scale)
		Matrix4 finalTransform = new Matrix4(instanceTransform).mul(tmp);

		return finalTransform;
	}
}
