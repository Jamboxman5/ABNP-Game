package me.jamboxman5.abnpgame.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.entity.LivingEntity;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

import javax.sound.sampled.Line;

public class Bullet extends Projectile{

	Ammo ammo;
	Firearm firedFrom;
	int hits;
	boolean drawFirst;
	boolean drawn;

	public Bullet(float rotation, int speed, Vector3 position, int range, Ammo fired, Firearm firedFrom, boolean drawFirst) {
        super(ABNPGame.getInstance());
        this.rotation = rotation;
		this.speed = speed;
		this.position = position;
		this.range = range;
		this.drawFirst = drawFirst;
		this.firedFrom = firedFrom;
		ammo = fired;
		hits = 0;
		drawn = false;
//		System.out.println("Bullet Start x: " + getScreenX());
//		System.out.println("Bullet Start y: " + getScreenY());
	}

	@Override
	public void update(float delta) {
		if (!drawn) return;
		if (traveled > range) ABNPGame.getInstance().getMapManager().disposeProjectile(this);
		int xComp = (int) (speed * Math.cos(rotation));
		int yComp = (int) (speed * Math.sin(rotation));

		Vector2[] collisionPoints = new Vector2[15];

		float x = position.x;
		float y = position.z;
		for (int i = 0; i < 15; i++) {
			collisionPoints[i] = new Vector2(x, y);
			x += xComp/15.0;
			y += yComp/15.0;
		}

		ABNPGame game = ABNPGame.getInstance();
		Array<LivingEntity> ignoring = new Array<>();
		for (Entity e : game.getMapManager().getEntities().toArray(Entity.class)) {
			if (!(e instanceof LivingEntity)) continue;
			if (e == gp.getPlayer()) continue;
			LivingEntity entity = (LivingEntity) e;
			for (Vector2 point : collisionPoints) {
				if (entity.getCollision().contains(point) && !ignoring.contains(entity, true)) {
					//bullet hit
					ignoring.add(entity);
					entity.damage(firedFrom.getDamage() * ammo.getDamageBoost());
					ammo.getImpactSound().play(Settings.sfxVolume);
					hits++;
					if (hits >= ammo.getBreachCount()) {
						ABNPGame.getInstance().getMapManager().disposeProjectile(this);
						break;
					}

				}
			}
			if (hits >= ammo.getBreachCount()) {
				break;
			}
		}

		position.x += xComp;
		position.z += yComp;
		traveled += speed;
	}


	@Override
	public void draw(DecalBatch batch, ShapeRenderer shape, PerspectiveCamera camera) {
		if (!drawFirst) {
			drawFirst = true;
			return;
		}
		drawn = true;

		// Use the camera’s projection matrix

		shape.begin(ShapeRenderer.ShapeType.Filled);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);
		// Use world coordinates directly
		float x = position.x;
		float y = position.z;

		float xComp = (float) (speed * Math.cos(rotation));
		float yComp = (float) (speed * Math.sin(rotation));

		shape.setColor(1f, 1f, 180f / 255f, 1f);
		shape.line(x, y,
				x + xComp * 2f,   y + yComp * 2f);

		shape.end();
	}


	public void shoot() {
		
	}

}
