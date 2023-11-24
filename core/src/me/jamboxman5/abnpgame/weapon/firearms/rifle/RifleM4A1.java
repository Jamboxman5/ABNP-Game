package me.jamboxman5.abnpgame.weapon.firearms.rifle;



import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.awt.image.BufferedImage;

public class RifleM4A1 extends Firearm {

	public RifleM4A1() {
		this(new WeaponModLoadout(), new StandardAmmo(), 30);
	}
	
	public RifleM4A1(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {
		BufferedImage[] iSprites = {setup("/resources/entity/player/rifle/Player_Rifle", .33)};
		BufferedImage[] sSprites = {setup("/resources/entity/player/rifle/Player_Rifle_Shoot_2", .33),
									setup("/resources/entity/player/rifle/Player_Rifle_Shoot_1", .33),
									setup("/resources/entity/player/rifle/Player_Rifle_Shoot_0", .33),
									setup("/resources/entity/player/rifle/Player_Rifle_Shoot_0", .33),
									setup("/resources/entity/player/rifle/Player_Rifle_Shoot_0", .33)};
		BufferedImage[] rSprites = {setup("/resources/entity/player/rifle/Player_Rifle", .33)};
		this.attackSound = "sfx/weapon/rifle/Assault_Rifle_Shot";
		this.reloadSound = "sfx/weapon/rifle/Assault_Rifle_Reload";
		this.attackRateMS = 86;
		this.damage = 30;
		this.equippedMods = mods;
		this.reloadSpeedMS = 2300;
		this.magSize = 30;
		this.range = 1000;
		this.idleSprites = iSprites;
		this.shootSprites = sSprites;
		this.reloadSprites = rSprites;
		this.activeSprites = idleSprites;
		this.hudSprite = setup("/resources/weapon/rifle/M4A1", .42);
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "M4A1";
		this.firingVelocity = 150;
		this.type = WeaponType.M4A1;
		this.yOffset = 18;
	}

}
