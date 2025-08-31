package me.jamboxman5.abnpgame.weapon.mods;

import me.jamboxman5.abnpgame.weapon.mods.firearm.ExtendedMagazine;
import me.jamboxman5.abnpgame.weapon.mods.firearm.ForeGrip;
import me.jamboxman5.abnpgame.weapon.mods.firearm.RedDotSight;
import me.jamboxman5.abnpgame.weapon.mods.firearm.Silencer;

public class WeaponMod {
	
	protected double recoilModifier;
	protected double damageModifier;
	protected double fireRateModifier;
	protected double bulletSpreadModifier;
	protected double rangeModifier;
	protected double magCapacityModifier;
	protected ModType type;

    public ModType getType() { return type; }

	public double getRecoilModifier() { return recoilModifier; }

	public enum ModType {
		RedDotSight, Silencer, ExtendedMagazine, ForeGrip;
	}
	
	public static WeaponMod getByType(ModType type) {
		switch(type) {
		case RedDotSight:
			return new RedDotSight();
		case Silencer:
			return new Silencer();
		case ForeGrip:
			return new ForeGrip();
		case ExtendedMagazine:
			return new ExtendedMagazine();
		}
		return new RedDotSight();
	}


	public double getDamageModifier() {
		return damageModifier;
	}

	public double getFireRateModifier() {
		return fireRateModifier;
	}

	public double getBulletSpreadModifier() {
		return bulletSpreadModifier;
	}

	public double getRangeModifier() {
		return rangeModifier;
	}

	public double getMagCapacityModifier() {
		return magCapacityModifier;
	}

	
}
