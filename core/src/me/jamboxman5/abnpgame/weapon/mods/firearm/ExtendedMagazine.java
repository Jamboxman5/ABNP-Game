package me.jamboxman5.abnpgame.weapon.mods.firearm;

import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;

public class ExtendedMagazine extends WeaponMod {
	public ExtendedMagazine() {
		accuracyModifier = 1;
		damageModifier = 1;
		fireRateModifier = 1;
		bulletSpreadModifier = 1;
		rangeModifier = 1;
		magCapacityModifier = 2;
		type = ModType.ExtendedMagazine;
	}
}
