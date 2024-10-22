package me.jamboxman5.abnpgame.weapon;

import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.ShellAmmo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.util.ArrayList;
import java.util.List;

public class WeaponLoadout {

	Array<Weapon> weapons;
	Array<Ammo> ammos;
	Weapon activeWeapon;
	
	public WeaponLoadout() {
		weapons = new Array<>();
		ammos = new Array<>();
		ammos.add(new StandardAmmo());
		ammos.add(new ShellAmmo());
		weapons.add(new RifleM4A1(new WeaponModLoadout(), getAmmo(Ammo.AmmoType.StandardAmmo), 30));
		weapons.add(new Pistol1911(new WeaponModLoadout(), getAmmo(Ammo.AmmoType.StandardAmmo), 30));
		weapons.add(new ShotgunWinchester12());
		activeWeapon = weapons.get(0);
	}
	public WeaponLoadout(Array<Weapon> weapons, Array<Ammo> ammos) {
		this.weapons = weapons;
		this.ammos = ammos;
		if (!weapons.isEmpty())  {
			activeWeapon = weapons.get(0);
			for (Firearm firearm : getFirearms()) {
				firearm.setAmmo(getAmmo(firearm.getAmmoType()));
			}
		}
	}
	public void nextWeapon() {
		int idx = weapons.indexOf(activeWeapon, false) + 1;
		if (idx >= weapons.size) idx = 0;
		activeWeapon = weapons.get(idx);
	}
	public void previousWeapon() {
		int idx = weapons.indexOf(activeWeapon, false) - 1;
		if (idx < 0) idx = weapons.size-1;
		activeWeapon = weapons.get(idx);
	}
	public void addWeapon(Weapon newWeapon, boolean makeActive) {
		weapons.add(newWeapon);
		if (makeActive) {
			activeWeapon = newWeapon;
		}
	}
	public void removeWeapon(Weapon toRemove) {
		if (!weapons.contains(toRemove, false)) return;
		if (activeWeapon.equals(toRemove)) {
			previousWeapon();
		}
		weapons.removeValue(toRemove, false);
	}
	public Weapon getActiveWeapon() { return activeWeapon; }
	public Firearm getActiveFirearm() {
		if (activeWeapon instanceof Firearm) {
			return (Firearm) activeWeapon;
		} else return null;
	}

    public Array<Weapon> getWeapons() { return weapons; }

    public Array<Firearm> getFirearms() {
		Array<Firearm> firearms = new Array<>();
		for (Weapon w : weapons) {
			if (w instanceof Firearm) firearms.add((Firearm) w);
		}
		return firearms;
    }

	public Array<Ammo> getAmmos() { return ammos; }
	public Ammo getAmmo(Ammo.AmmoType type) {
		for (Ammo ammo : ammos) {
			if (ammo.getType() == type) return ammo;
		}
		return null;
	}
}
