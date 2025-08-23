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

public class WeaponLoadout {

	Array<Weapon> equippedWeapons;
	Array<Weapon> ownedWeapons;
	Array<Ammo> ammos;
	Weapon activeWeapon;
	
	public WeaponLoadout() {
		equippedWeapons = new Array<>();
		ammos = new Array<>();
		ammos.add(new StandardAmmo());
		ammos.add(new ShellAmmo());
		equippedWeapons.add(new RifleM4A1());
		equippedWeapons.add(new Pistol1911());
		equippedWeapons.add(new ShotgunWinchester12());
		activeWeapon = equippedWeapons.get(0);
	}

	public void setEquippedWeapons(Array<Weapon> weapons) {
		this.equippedWeapons = weapons;
		activeWeapon = equippedWeapons.get(0);
	}


	public void setAmmos(Array<Ammo> ammos) { this.ammos = ammos; }

	public WeaponLoadout(Array<Weapon> weapons, Array<Ammo> ammos) {
		this.equippedWeapons = weapons;
		this.ammos = ammos;
		if (!weapons.isEmpty())  {
			activeWeapon = weapons.get(0);
			for (Firearm firearm : getEquippedFirearms()) {
				firearm.setAmmo(getAmmo(firearm.getAmmoType()));
			}
		}
	}
	public void nextWeapon() {
		int idx = equippedWeapons.indexOf(activeWeapon, false) + 1;
		if (idx >= equippedWeapons.size) idx = 0;
		activeWeapon = equippedWeapons.get(idx);
	}
	public void previousWeapon() {
		int idx = equippedWeapons.indexOf(activeWeapon, false) - 1;
		if (idx < 0) idx = equippedWeapons.size-1;
		activeWeapon = equippedWeapons.get(idx);
	}
	public void addWeapon(Weapon newWeapon, boolean makeActive, boolean keep) {
		equippedWeapons.add(newWeapon);
		if (makeActive) {
			activeWeapon = newWeapon;
		}
		if (keep) {
			ownedWeapons.add(newWeapon);
		}
	}
	public void removeWeapon(Weapon toRemove, boolean permanent) {
		if (!equippedWeapons.contains(toRemove, false)) return;
		if (activeWeapon.equals(toRemove)) {
			previousWeapon();
		}
		equippedWeapons.removeValue(toRemove, false);
		if (permanent) {
			ownedWeapons.removeValue(toRemove, false);
		}
	}
	public Weapon getActiveWeapon() { return activeWeapon; }
	public Firearm getActiveFirearm() {
		if (activeWeapon instanceof Firearm) {
			return (Firearm) activeWeapon;
		} else return null;
	}

    public Array<Weapon> getEquippedWeapons() { return equippedWeapons; }

    public Array<Firearm> getEquippedFirearms() {
		Array<Firearm> firearms = new Array<>();
		for (Weapon w : equippedWeapons) {
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

	public void setOwnedWeapons(Array<Weapon> weapons) {
		ownedWeapons = weapons;
	}

	public Array<Weapon> getOwnedWeapons() { return ownedWeapons; }
}
