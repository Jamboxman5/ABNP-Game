package me.jamboxman5.abnpgame.entity.projectile;

import java.awt.*;

public abstract class Projectile {

	protected int speed;
	protected int range;
	protected double worldX;
	protected double worldY;
	protected double rotation;
	protected int traveled = 0;

	public abstract void update();

	public abstract void draw(Graphics2D g2);
	
}
