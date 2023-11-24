package me.jamboxman5.abnpgame.weapon.firearms.shotgun;

import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.ShellAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.awt.image.BufferedImage;

public class ShotgunWinchester12 extends Firearm {

	public ShotgunWinchester12() {
		this(new WeaponModLoadout(), new ShellAmmo(), 6);
	}
	
	public ShotgunWinchester12(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {
		BufferedImage[] iSprites = {setup("/resources/entity/player/shotgun/Player_Shotgun", .33)};
		BufferedImage[] sSprites = {setup("/resources/entity/player/shotgun/Player_Shotgun_Shoot_2", .33),
									setup("/resources/entity/player/shotgun/Player_Shotgun_Shoot_2", .33),
									setup("/resources/entity/player/shotgun/Player_Shotgun_Shoot_1", .33),
									setup("/resources/entity/player/shotgun/Player_Shotgun_Shoot_1", .33),
									setup("/resources/entity/player/shotgun/Player_Shotgun_Shoot_0", .33),
									setup("/resources/entity/player/shotgun/Player_Shotgun_Shoot_0", .33),
									setup("/resources/entity/player/shotgun/Player_Shotgun_Shoot_0", .33)};
		BufferedImage[] rSprites = {setup("/resources/entity/player/shotgun/Player_Shotgun", .33)};
		this.attackSound = "sfx/weapon/shotgun/Shotgun_Shot";
		this.reloadSound = "sfx/weapon/rifle/Assault_Rifle_Reload";
		this.attackRateMS = 1100;
		this.damage = 85;
		this.equippedMods = mods;
		this.reloadSpeedMS = 2300;
		this.magSize = 6;
		this.range = 350;
		this.idleSprites = iSprites;
		this.shootSprites = sSprites;
		this.reloadSprites = rSprites;
		this.activeSprites = idleSprites;
		this.hudSprite = setup("/resources/weapon/shotgun/Winchester12", .27);
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "Winchester 12GA";
		this.firingVelocity = 150;
		this.type = WeaponType.WINCHESTER12;
		this.yOffset = 18;
	}

}
