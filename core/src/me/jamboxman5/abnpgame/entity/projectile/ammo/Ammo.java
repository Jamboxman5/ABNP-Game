package me.jamboxman5.abnpgame.entity.projectile.ammo;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

	public static Ammo getByType(AmmoType type) {
		switch(type) {
			case ShellAmmo:
				return new ShellAmmo();
			case StandardAmmo:
				return new StandardAmmo();
			case SlugAmmo:
				return new SlugAmmo();
			default:
				return new StandardAmmo();
		}
	}

	public void shoot(double rotation, Firearm weapon, Vector3 start, boolean drawFirst) {
		double[] rotations = new double[shots];

		for (int i = 0; i < shots; i++) {

			double spreadRandom = (Math.random()/15f) * spread;
			if (Math.random() > .5) spreadRandom = -spreadRandom;
			rotations[i] = rotation + spreadRandom;

		}

		for (int i = 0; i < rotations.length; i++) {
			Bullet bullet = new Bullet((float) rotations[i],
					(int)(weapon.getFiringVelocity() * speedBoost),
					start,
					(int)(weapon.getRange() * rangeBoost), this, weapon, drawFirst);
			ABNPGame.getInstance().getMapManager().addProjectile(bullet);
		}

	}

	public int getAmmoCount() { return ammoCount; }

	public void remove(int magSize) { ammoCount -= magSize; }

	public Sound getImpactSound() { return impactSound; }

	public int getBreachCount() { return breachCount; }

	public void addAmmo(int rounds) { ammoCount += rounds;
	}

	public void setCount(int count) {
		ammoCount = count;
	}

	public double getDamageBoost() { return damageBoost; }

	public enum AmmoType {
		StandardAmmo, ShellAmmo, SlugAmmo;
	}

	public AmmoType getType() { return type; }

}
