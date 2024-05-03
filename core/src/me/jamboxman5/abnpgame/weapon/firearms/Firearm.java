package me.jamboxman5.abnpgame.weapon.firearms;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import me.jamboxman5.abnpgame.entity.player.Survivor;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.Weapon;

public class Firearm extends Weapon {
	
	protected int magSize;
	protected int loaded;
	protected int reloadSpeedMS;
	protected int range;
	protected int firingVelocity;


	protected Sound reloadSound;
	protected Sound outOfAmmoSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/misc/Out_Of_Ammo.wav"));
	protected long lastMisfire = System.currentTimeMillis();

	protected Ammo currentAmmo;


	public int getLoadedAmmo() { return loaded; }
	public int getAmmoCount() { return currentAmmo.getAmmoCount(); }
	public boolean shoot(Survivor shooter, double offset) {
		if (loaded <= 0) {
			if (canReload()) reload();
			else {
				if (!reloading && (System.currentTimeMillis() - lastMisfire) > 250) {
					outOfAmmoSound.play();
					lastMisfire = System.currentTimeMillis();
				}
				return false;
			}
		}

		if (!canAttack()) return false;
//		if (!(shooter instanceof Player)) return false;
		ABNPGame gp = ABNPGame.getInstance();
		activeSprites = shootSprites;
		shooter.setAnimFrame(shootSprites.size-1);
		attackSound.play();
		this.lastAttack = System.currentTimeMillis();
		loaded -= 1;
		currentAmmo.shoot(shooter.getAimAngle() + offset,
						  this, 
						  shooter.getPosition());
//		Bullet bullet = new Bullet(gp.getPlayer().getAdjustedRotation(),
//				150, 
//				gp.getPlayer().getAdjustedWorldX(), 
//				gp.getPlayer().getAdjustedWorldY(),  
//				150);
//		bullet.shoot();
		return true;
	}

	public boolean canReload() {
		return (!reloading && (loaded < magSize) && (currentAmmo.getAmmoCount() > 0));
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
					int ammoCount = currentAmmo.getAmmoCount();
					if (delta > ammoCount) {
						delta = ammoCount;
					}
					loaded += delta;
					currentAmmo.remove(delta);
					reloading = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
			}
		}.start();
	}
	
	@Override
	public boolean attack(Survivor attacker, double offset) {
		return shoot(attacker, offset);
	}
	public int getFiringVelocity() { return firingVelocity; }
	public int getRange() { return range; }
	public Ammo.AmmoType getAmmoType() { return currentAmmo.getType(); }

	public void buyMag(int magCount) {
		currentAmmo.addAmmo(magSize*magCount);
	}

	public boolean isEmpty() { return loaded + currentAmmo.getAmmoCount() == 0; }

}
