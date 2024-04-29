package me.jamboxman5.abnpgame.entity.projectile;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import me.jamboxman5.abnpgame.main.ABNPGame;

import java.awt.*;


public class Bullet extends Projectile{

	public Bullet(double rotation, int speed, double x, double y, int range) {
		this.rotation = rotation;
		this.speed = speed;
		this.worldX = x;
		this.worldY = y;
		this.range = range;
//		System.out.println("Bullet Start x: " + getScreenX());
//		System.out.println("Bullet Start y: " + getScreenY());
	}

	@Override
	public void update() {
		if (traveled > range) ABNPGame.getInstance().getMapManager().disposeProjectile(this);
		int xComp = (int) (speed * Math.cos(rotation));
		int yComp = (int) (speed * Math.sin(rotation));
		
		int[] xPoints = {(int) worldX, (int) (worldX+xComp)};
		int[] yPoints = {(int) worldY, (int) (worldY+yComp)};
		Polygon bulletCollision = new Polygon(xPoints, yPoints, 2);
		
		worldX += xComp;
		worldY += yComp;
		traveled += speed;
	}

	@Override
	public void draw(ShapeRenderer renderer) {
		ABNPGame gp = ABNPGame.getInstance();
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		int x = (int) (worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX());
        int y = (int) (worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY());
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
