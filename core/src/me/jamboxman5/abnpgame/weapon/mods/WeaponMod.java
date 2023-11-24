package me.jamboxman5.abnpgame.weapon.mods;

public class WeaponMod {
	
	protected double accuracyModifier;
	protected double damageModifier;
	protected double fireRateModifier;
	protected double bulletSpreadModifier;
	protected double rangeModifier;
	protected ModType type;

    public ModType getType() { return type; }

    public enum ModType {
		RedDotSight, Silencer;
	}
	
	public static WeaponMod getByType(ModType type) {
		switch(type) {
		case RedDotSight:
			return new RedDotSight();
		case Silencer:
			return new Silencer();
		}
		return new RedDotSight();
	}
	
}
