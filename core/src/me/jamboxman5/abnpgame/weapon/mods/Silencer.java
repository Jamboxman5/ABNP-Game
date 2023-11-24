package me.jamboxman5.abnpgame.weapon.mods;

public class Silencer extends WeaponMod {
	public Silencer() {
		accuracyModifier = 1.2;
		damageModifier = .6;
		fireRateModifier = 1;
		bulletSpreadModifier = 1;
		rangeModifier = .6;
		type = ModType.Silencer;
	}
}
