package me.jamboxman5.abnpgame.weapon.firearms;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.Weapon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Firearm extends Weapon {
	
	protected int magSize;
	protected int loaded;
	protected int reloadSpeedMS;
	protected int range;
	protected int firingVelocity;


	protected Sound reloadSound;
	
	protected Ammo currentAmmo;


	public int getLoadedAmmo() { return loaded; }
	public int getAmmoCount() { return currentAmmo.getAmmoCount(); }
	public void shoot() {
		if (loaded <= 0 && !reloading) {
			reload(); return;
		}
		if (!canAttack()) return;
		ABNPGame gp = ABNPGame.getInstance();
		activeSprites = shootSprites;
		gp.getPlayer().setAnimFrame(shootSprites.size-1);
		attackSound.play();
		this.lastAttack = System.currentTimeMillis();
		loaded -= 1;
		currentAmmo.shoot(gp.getPlayer().getAdjustedRotation(), 
						  this, 
						  (int)gp.getPlayer().getWorldX(),
						  (int)gp.getPlayer().getWorldY());
//		Bullet bullet = new Bullet(gp.getPlayer().getAdjustedRotation(), 
//				150, 
//				gp.getPlayer().getAdjustedWorldX(), 
//				gp.getPlayer().getAdjustedWorldY(),  
//				150);
//		bullet.shoot();
	}

	public boolean canReload() {
		return (!reloading && (loaded < magSize));
	}

	public void reload() {
		reloading = true;
		activeSprites = reloadSprites;
		ABNPGame.getInstance().getPlayer().setAnimFrame(reloadSprites.size-1);
		reloadSound.play();
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(reloadSpeedMS);
					int delta = magSize - loaded;
					loaded = magSize;
					currentAmmo.remove(delta);
					reloading = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
			}
		}.start();
	}
	
	@Override
	public void attack() {
		shoot();
	}
	public int getFiringVelocity() { return firingVelocity; }
	public int getRange() { return range; }
	public Ammo.AmmoType getAmmoType() { return currentAmmo.getType(); }

}
