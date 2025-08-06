package me.jamboxman5.abnpgame.entity.projectile;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.entity.LivingEntity;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Settings;

public class Bullet extends Projectile{

	Ammo ammo;
	int hits;
	boolean drawFirst;

	public Bullet(double rotation, int speed, Vector2 position, int range, Ammo fired, boolean drawFirst) {
		this.rotation = rotation;
		this.speed = speed;
		this.worldX = position.x;
		this.worldY = position.y;
		this.range = range;
		this.drawFirst = drawFirst;
		ammo = fired;
		hits = 0;
//		System.out.println("Bullet Start x: " + getScreenX());
//		System.out.println("Bullet Start y: " + getScreenY());
	}

	@Override
	public void update() {
		if (traveled > range) ABNPGame.getInstance().getMapManager().disposeProjectile(this);
		int xComp = (int) (speed * Math.cos(rotation));
		int yComp = (int) (speed * Math.sin(rotation));

		Vector2[] collisionPoints = new Vector2[15];

		float x = (float) worldX;
		float y = (float) worldY;
		for (int i = 0; i < 15; i++) {
			collisionPoints[i] = new Vector2(x, y);
			x += xComp/15.0;
			y += yComp/15.0;
		}

		ABNPGame game = ABNPGame.getInstance();
		Array<LivingEntity> ignoring = new Array<>();
		for (Entity e : game.getMapManager().getEntities()) {
			if (!(e instanceof LivingEntity)) continue;
			LivingEntity entity = (LivingEntity) e;
			for (Vector2 point : collisionPoints) {
				if (entity.getCollision().contains(point) && !ignoring.contains(entity, true)) {
					//bullet hit
					ignoring.add(entity);
					entity.damage(10);
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

		worldX += xComp;
		worldY += yComp;
		traveled += speed;
	}

	@Override
	public void draw(ShapeRenderer renderer) {
		if (!drawFirst) {
			drawFirst = true;
			return;
		}
		ABNPGame gp = ABNPGame.getInstance();
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		int x = (int) (((worldX - gp.getPlayer().getWorldX())*.5) + gp.getPlayer().getScreenX());
        int y = (int) (((worldY - gp.getPlayer().getWorldY())*.5) + gp.getPlayer().getScreenY());

//        x *= gp.getZoom();
//        y *= gp.getZoom();
		int xComp = (int) (speed * Math.cos(rotation));
		int yComp = (int) (speed * Math.sin(rotation));
		renderer.setColor(new Color((255f/255f),(255f/255f),(180f/255f), 1f));
//		g2.setStroke(new BasicStroke(2));
		renderer.line(x-(xComp/2), y-(yComp/2), (int)x+xComp*2, (int)y+yComp*2);
		renderer.end();

	}
	
	public void shoot() {
		
	}

}
