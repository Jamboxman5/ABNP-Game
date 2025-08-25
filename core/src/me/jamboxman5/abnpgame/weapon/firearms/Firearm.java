package me.jamboxman5.abnpgame.weapon.firearms;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.entity.mob.npc.Ally;
import me.jamboxman5.abnpgame.entity.mob.player.OnlinePlayer;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;

public class Firearm extends Weapon {
	
	protected int magSize;
	protected int loaded;
	protected int reloadSpeedMS;
	protected int range;
	protected int firingVelocity;

	protected Sound reloadSound;
	protected Sound outOfAmmoSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/misc/Out_Of_Ammo.wav"));
	protected Sound silencerSound;
	protected long lastMisfire = System.currentTimeMillis();

	protected Ammo currentAmmo;


	public int getLoadedAmmo() { return loaded; }
	public int getAmmoCount() { return currentAmmo.getAmmoCount(); }
	public boolean shoot(Survivor shooter, double offset) {
		if (loaded <= 0) {
			if (canReload()) reload();
			else {
				if (!reloading && (System.currentTimeMillis() - lastMisfire) > 250) {
					outOfAmmoSound.play(Settings.sfxVolume);
					lastMisfire = System.currentTimeMillis();
				}
				return false;
			}
		}

		if (!canAttack()) return false;
//		if (!(shooter instanceof Player)) return false;
		ABNPGame gp = ABNPGame.getInstance();
		currentAnimation = shootAnimation;
		if (hasMod(WeaponMod.ModType.Silencer)) {
			silencerSound.play(Settings.sfxVolume);
		} else {
			attackSound.play(Settings.sfxVolume);
		}
		this.lastAttack = System.currentTimeMillis();
		loaded -= 1;

		boolean drawFirst = true;

		Vector3 shootPos = shooter.getPosition().cpy();
		if ((shooter instanceof Ally) || (shooter instanceof OnlinePlayer)) {
			drawFirst = false;
		}

		if (shooter instanceof OnlinePlayer) {
			OnlinePlayer p = (OnlinePlayer) shooter;
			currentAmmo.shoot(p.getRotation() + offset,
					this,
					new Vector2(shootPos.x, shootPos.z), drawFirst);
			return true;
		}
		currentAmmo.shoot(shooter.getAimAngle() + offset,
						  this,
				new Vector2(shootPos.x, shootPos.z), drawFirst);
//		Bullet bullet = new Bullet(gp.getPlayer().getAdjustedRotation(),
//				150, 
//				gp.getPlayer().getAdjustedWorldX(), 
//				gp.getPlayer().getAdjustedWorldY(),  
//				150);
//		bullet.shoot();
		return true;
	}

	public boolean fakeShoot(Survivor shooter) {
		if (!canAttack()) return false;

		currentAnimation = shootAnimation;
		if (hasMod(WeaponMod.ModType.Silencer)) {
			silencerSound.play(Settings.sfxVolume);
		} else {
			attackSound.play(Settings.sfxVolume);

		}
		this.lastAttack = System.currentTimeMillis();

		return true;
	}

	public boolean canReload() {
		return (!reloading && (loaded < getAdjustedMagSize()) && (currentAmmo.getAmmoCount() > 0));
	}

	public int getAdjustedMagSize() {
		float adjustedCapacity = magSize;
		if (hasMod(WeaponMod.ModType.ExtendedMagazine)) adjustedCapacity *= (float) WeaponMod.getByType(WeaponMod.ModType.ExtendedMagazine).getMagCapacityModifier();
		return Math.round(adjustedCapacity);
	}

	public void reload() {
		reloading = true;
		currentAnimation = reloadAnimation;
		reloadSound.play(Settings.sfxVolume);
		final int loadTo = getAdjustedMagSize();

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(reloadSpeedMS);
					int delta = loadTo - loaded;
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
	@Override
	public boolean fakeAttack(Survivor attacker) {
		return fakeShoot(attacker);
	}
	public int getFiringVelocity() { return firingVelocity; }
	public int getRange() { return range; }
	public Ammo.AmmoType getAmmoType() { return currentAmmo.getType(); }

	public void buyMag(int magCount) {
		currentAmmo.addAmmo(magSize*magCount);
	}

	public boolean isEmpty() { return loaded + currentAmmo.getAmmoCount() == 0; }

	public void setLoadedAmmo(int loaded) { this.loaded = loaded; }

	public void setAmmo(Ammo newAmmo) { currentAmmo = newAmmo;
	}

	public void melee() {
		currentAnimation = meleeAnimation;
	}
}
