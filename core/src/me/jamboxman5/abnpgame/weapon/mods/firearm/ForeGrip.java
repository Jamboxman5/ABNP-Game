package me.jamboxman5.abnpgame.weapon.mods.firearm;

import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;

public class ForeGrip extends WeaponMod {
	public ForeGrip() {
		recoilModifier = .5;
		damageModifier = 1;
		fireRateModifier = 1;
		bulletSpreadModifier = 1;
		rangeModifier = 1;
		magCapacityModifier = 1;
		type = ModType.ForeGrip;
	}
}
