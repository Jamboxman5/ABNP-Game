package me.jamboxman5.abnpgame.entity.projectile;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.main.ABNPGame;

import java.awt.*;

public abstract class Projectile extends Entity {

	protected int speed;
	protected int range;

	protected int traveled = 0;

	public Projectile(ABNPGame gamePanel) {
		super(gamePanel);
	}

}
