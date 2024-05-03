package me.jamboxman5.abnpgame.entity.projectile.ammo;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.projectile.Bullet;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public abstract class Ammo {

	protected AmmoType type;
	protected double damageBoost;
	protected double speedBoost;
	protected double rangeBoost;
	protected double spread;
	protected int shots;
	protected int ammoCount;
	protected Sound impactSound;
	protected int breachCount;
	
	public void shoot(double rotation, Firearm weapon, Vector2 start) {
		double[] rotations = new double[shots];

		for (int i = 0; i < shots; i++) {

			double spreadRandom = (Math.random()/15) * spread;
			if (Math.random() > .5) spreadRandom = -spreadRandom;
			rotations[i] = rotation + spreadRandom;

		}

		for (int i = 0; i < rotations.length; i++) {
			Bullet bullet = new Bullet(rotations[i],
					(int)(weapon.getFiringVelocity() * speedBoost),
					start,
					(int)(weapon.getRange() * rangeBoost), this);
			ABNPGame.getInstance().getMapManager().addProjectile(bullet);
		}

	}

	public int getAmmoCount() { return ammoCount; }

	public void remove(int magSize) { ammoCount -= magSize; }

	public Sound getImpactSound() { return impactSound; }

	public int getBreachCount() { return breachCount; }

	public void addAmmo(int rounds) { ammoCount += rounds;
	}

	public enum AmmoType {
		StandardAmmo, ShellAmmo;
	}

	public AmmoType getType() { return type; }

}
