package me.jamboxman5.abnpgame.weapon.mods.firearm;

import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;

public class Silencer extends WeaponMod {
	public Silencer() {
		recoilModifier = 1.2;
		damageModifier = .6;
		fireRateModifier = 1;
		bulletSpreadModifier = 1;
		rangeModifier = .6;
		magCapacityModifier = 1;
		type = ModType.Silencer;
	}
}
