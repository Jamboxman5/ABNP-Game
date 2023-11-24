package me.jamboxman5.abnpgame.weapon.mods;

public class RedDotSight extends WeaponMod {
	
	public RedDotSight() {
		accuracyModifier = 1.5;
		damageModifier = 1;
		fireRateModifier = 1;
		bulletSpreadModifier = 1;
		rangeModifier = 1;
		type = ModType.RedDotSight;
	}

}
