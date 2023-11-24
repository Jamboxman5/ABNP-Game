package me.jamboxman5.abnpgame.weapon.firearms.pistol;

import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.awt.image.BufferedImage;

public class Pistol1911 extends Firearm {

	public Pistol1911() {
		this(new WeaponModLoadout(), new StandardAmmo(), 8);
	}
	
	public Pistol1911(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {
		BufferedImage[] iSprites = {setup("/resources/entity/player/handgun/Player_Handgun", .33)};
		BufferedImage[] sSprites = {setup("/resources/entity/player/handgun/Player_Handgun_Shoot_2", .33),
									setup("/resources/entity/player/handgun/Player_Handgun_Shoot_2", .33),
									setup("/resources/entity/player/handgun/Player_Handgun_Shoot_1", .33),
									setup("/resources/entity/player/handgun/Player_Handgun_Shoot_1", .33),
									setup("/resources/entity/player/handgun/Player_Handgun_Shoot_0", .33),
									setup("/resources/entity/player/handgun/Player_Handgun_Shoot_0", .33),
									setup("/resources/entity/player/handgun/Player_Handgun_Shoot_0", .33)};
		BufferedImage[] rSprites = {setup("/resources/entity/player/handgun/Player_Handgun", .33)};
		this.attackSound = "sfx/weapon/pistol/Pistol_Shot";
		this.reloadSound = "sfx/weapon/rifle/Assault_Rifle_Reload";
		this.attackRateMS = 350;
		this.damage = 18;
		this.equippedMods = mods;
		this.reloadSpeedMS = 1500;
		this.magSize = 8;
		this.range = 480;
		this.idleSprites = iSprites;
		this.shootSprites = sSprites;
		this.reloadSprites = rSprites;
		this.activeSprites = idleSprites;
		this.hudSprite = setup("/resources/weapon/pistol/1911", .33);
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "M1911";
		this.firingVelocity = 150;
		this.type = WeaponType.M1911;
		this.yOffset = 13;

	}
	
}
