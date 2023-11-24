package me.jamboxman5.abnpgame.weapon;

import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;

import java.util.ArrayList;
import java.util.List;

public class WeaponLoadout {

	Array<Weapon> weapons;
	Weapon activeWeapon;
	
	public WeaponLoadout() {
		weapons = new Array<>();
		weapons.add(new RifleM4A1());
		weapons.add(new Pistol1911());
		weapons.add(new ShotgunWinchester12());
		activeWeapon = weapons.get(0);
	}
	public WeaponLoadout(Array<Weapon> weapons) {
		this.weapons = weapons;
		if (!weapons.isEmpty()) activeWeapon = weapons.get(0);
	}
	public void nextWeapon() {
		int idx = weapons.indexOf(activeWeapon) + 1;
		if (idx >= weapons.size()) idx = 0;
		activeWeapon = weapons.get(idx);
	}
	public void previousWeapon() {
		int idx = weapons.indexOf(activeWeapon) - 1;
		if (idx < 0) idx = weapons.size()-1;
		activeWeapon = weapons.get(idx);
	}
	public void addWeapon(Weapon newWeapon, boolean makeActive) {
		weapons.add(newWeapon);
		if (makeActive) {
			activeWeapon = newWeapon;
		}
	}
	public void removeWeapon(Weapon toRemove) {
		if (!weapons.contains(toRemove)) return;
		if (activeWeapon.equals(toRemove)) {
			previousWeapon();
		}
		weapons.remove(toRemove);
	}
	public Weapon getActiveWeapon() { return activeWeapon; }
	public Firearm getActiveFirearm() {
		if (activeWeapon instanceof Firearm f) {
			return f;
		} else return null;
	}

    public List<Weapon> getWeapons() { return weapons; }
}
