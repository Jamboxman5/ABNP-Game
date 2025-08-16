package me.jamboxman5.abnpgame.weapon.firearms.shotgun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.ShellAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class ShotgunWinchester12 extends Firearm {

	public static Array<Sprite> idleSprites;
	public static Array<Sprite> shootSprites;
	public static Array<Sprite> reloadSprites;
	public static Sprite hudSprite;

	public static Sound attackSound;
	public static Sound reloadSound;

	public ShotgunWinchester12() {
		this(new WeaponModLoadout(), new ShellAmmo(), 6);
	}
	
	public ShotgunWinchester12(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {

		super.shootSprites = shootSprites;
		super.reloadSprites = reloadSprites;
		super.idleSprites = idleSprites;
		super.hudSprite = hudSprite;

		super.attackSound = attackSound;
		super.reloadSound = reloadSound;

		this.attackRateMS = 1100;
		this.damage = 85;
		this.equippedMods = mods;
		this.reloadSpeedMS = 2300;
		this.magSize = 6;
		this.range = 350;
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "Winchester 12GA";
		this.firingVelocity = 150;
		this.type = me.jamboxman5.abnpgame.weapon.WeaponType.WINCHESTER12;
		this.xOffset = 18;
		this.yOffset = 12;
		this.recoil = 5;

		activeSprites = super.idleSprites;

	}
	public static void loadAssets(AssetManager assets) {
		// Sprites (Textures)
		assets.load("weapon/shotgun/Winchester12.png", Texture.class);
		assets.load("entity/player/shotgun/Player_Shotgun.png", Texture.class);
		assets.load("entity/player/shotgun/Player_Shotgun_Shoot_2.png", Texture.class);
		assets.load("entity/player/shotgun/Player_Shotgun_Shoot_1.png", Texture.class);
		assets.load("entity/player/shotgun/Player_Shotgun_Shoot_0.png", Texture.class);

		// Sounds
		assets.load("sound/sfx/weapon/shotgun/Shotgun_Shot.wav", Sound.class);
		assets.load("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

	public static void loadSprites(AssetManager assets) {
		hudSprite = setup("weapon/shotgun/Winchester12.png", assets, .35f);

		idleSprites = new Array<>(new Sprite[]{
				setup("entity/player/shotgun/Player_Shotgun.png", assets, .25f)
		});

		shootSprites = new Array<>(new Sprite[]{
				setup("entity/player/shotgun/Player_Shotgun_Shoot_2.png", assets, .25f),
				setup("entity/player/shotgun/Player_Shotgun_Shoot_2.png", assets, .25f),
				setup("entity/player/shotgun/Player_Shotgun_Shoot_1.png", assets, .25f),
				setup("entity/player/shotgun/Player_Shotgun_Shoot_1.png", assets, .25f),
				setup("entity/player/shotgun/Player_Shotgun_Shoot_1.png", assets, .25f),
				setup("entity/player/shotgun/Player_Shotgun_Shoot_0.png", assets, .25f),
				setup("entity/player/shotgun/Player_Shotgun_Shoot_0.png", assets, .25f)
		});

		reloadSprites = new Array<>(new Sprite[]{
				setup("entity/player/shotgun/Player_Shotgun.png", assets, .25f)
		});

		attackSound = assets.get("sound/sfx/weapon/shotgun/Shotgun_Shot.wav", Sound.class);
		reloadSound = assets.get("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

}
