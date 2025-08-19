package me.jamboxman5.abnpgame.weapon.mods;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class WeaponModLoadout {

	Array<WeaponMod> equippedMods;
	
	public WeaponModLoadout(Array<WeaponMod> mods) {
		equippedMods = mods;
	}
	public WeaponModLoadout() {
		equippedMods = new Array<>();
	}
	public void addMod(WeaponMod mod) { 
		if (!equippedMods.contains(mod, false)) equippedMods.add(mod);
	}
	public void removeMod(WeaponMod mod) {
		if (equippedMods.contains(mod, false)) equippedMods.removeValue(mod, false);
	}
	public Array<WeaponMod> getMods() { return equippedMods; }
	public boolean hasMod(WeaponMod.ModType type) {
		for (WeaponMod mod : equippedMods) {
			if (mod.getType() == type) return true;
		}
		return false;
	}
	
}
