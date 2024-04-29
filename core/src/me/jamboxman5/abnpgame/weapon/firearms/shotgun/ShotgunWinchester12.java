package me.jamboxman5.abnpgame.weapon.firearms.shotgun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.ShellAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class ShotgunWinchester12 extends Firearm {

	public ShotgunWinchester12() {
		this(new WeaponModLoadout(), new ShellAmmo(), 6);
	}
	
	public ShotgunWinchester12(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {
		idleSprites = new Array<>(new Sprite[]{setup("entity/player/shotgun/Player_Shotgun", null)});
		shootSprites = new Array<>(new Sprite[]{setup("entity/player/shotgun/Player_Shotgun_Shoot_2", null),
									setup("entity/player/shotgun/Player_Shotgun_Shoot_2", null),
									setup("entity/player/shotgun/Player_Shotgun_Shoot_1", null),
									setup("entity/player/shotgun/Player_Shotgun_Shoot_1", null),
									setup("entity/player/shotgun/Player_Shotgun_Shoot_0", null),
									setup("entity/player/shotgun/Player_Shotgun_Shoot_0", null),
									setup("entity/player/shotgun/Player_Shotgun_Shoot_0", null)});
		reloadSprites = new Array<>(new Sprite[]{setup("entity/player/shotgun/Player_Shotgun", null)});
		this.attackSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/shotgun/Shotgun_Shot.wav"));
		this.reloadSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav"));
		this.attackRateMS = 1100;
		this.damage = 85;
		this.equippedMods = mods;
		this.reloadSpeedMS = 2300;
		this.magSize = 6;
		this.range = 350;
		this.activeSprites = idleSprites;
		this.hudSprite = setup("weapon/shotgun/Winchester12", .35f);
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "Winchester 12GA";
		this.firingVelocity = 150;
		this.type = WeaponType.WINCHESTER12;
		this.xOffset = 18;
		this.yOffset = 12;
	}

}
