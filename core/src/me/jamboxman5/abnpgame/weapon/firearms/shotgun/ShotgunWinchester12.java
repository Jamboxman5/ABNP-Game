package me.jamboxman5.abnpgame.weapon.firearms.shotgun;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.ShellAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class ShotgunWinchester12 extends Firearm {

	public static Animation<TextureRegion> idleAnimation;
	public static Animation<TextureRegion> shootAnimation;
	public static Animation<TextureRegion> reloadAnimation;
	public static Animation<TextureRegion> meleeAnimation;
	public static Animation<TextureRegion> moveAnimation;

	public static Sprite hudSprite;

	public static Sound attackSound;
	public static Sound reloadSound;

	public ShotgunWinchester12() {
		this(new WeaponModLoadout(), new ShellAmmo(), 6);
	}
	
	public ShotgunWinchester12(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {

		super.shootAnimation = shootAnimation;
		super.reloadAnimation = reloadAnimation;
		super.idleAnimation = idleAnimation;
		super.moveAnimation = moveAnimation;
		super.meleeAnimation = meleeAnimation;

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
		this.xOffset = 0;
		this.yOffset = 12;
		this.shootXOffset = 8;
		this.shootYOffset = 12;
		this.meleeXOffset = 0;
		this.meleeYOffset = 14;
		this.recoil = 5;

		currentAnimation = super.idleAnimation;

	}
	public static void loadAssets(AssetManager assets) {
		// Sprites (Textures)
		assets.load("weapon/shotgun/Winchester12.png", Texture.class);

		assets.load("entity/player/shotgun/shoot/Player_Shotgun_Shoot_2.png", Texture.class);
		assets.load("entity/player/shotgun/shoot/Player_Shotgun_Shoot_1.png", Texture.class);
		assets.load("entity/player/shotgun/shoot/Player_Shotgun_Shoot_0.png", Texture.class);

		for (int i = 0; i < 20; i++) {
			assets.load("entity/player/shotgun/idle/Player_Shotgun_Idle_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 15; i++) {
			assets.load("entity/player/shotgun/melee/Player_Shotgun_Melee_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 20; i++) {
			assets.load("entity/player/shotgun/move/Player_Shotgun_Move_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 15; i++) {
			assets.load("entity/player/shotgun/reload/Player_Shotgun_Reload_" + i + ".png", Texture.class);
		}

		// Sounds
		assets.load("sound/sfx/weapon/shotgun/Shotgun_Shot.wav", Sound.class);
		assets.load("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

	public static void loadSprites(AssetManager assets) {
		hudSprite = setup("weapon/shotgun/Winchester12.png", assets, .15f);

		Array<TextureRegion> shootFrames = new Array<>();
		for (int i = 0; i < 3; i++) {
			shootFrames.add(new TextureRegion(assets.get("entity/player/shotgun/shoot/Player_Shotgun_Shoot_" + i + ".png", Texture.class)));
		}
		shootAnimation = new Animation<>(0.05f, shootFrames, Animation.PlayMode.NORMAL);


		Array<TextureRegion> idleFrames = new Array<>();
		for (int i = 0; i < 20; i++) {
			idleFrames.add(new TextureRegion(assets.get("entity/player/shotgun/idle/Player_Shotgun_Idle_" + i + ".png", Texture.class)));
		}
		idleAnimation = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);


		Array<TextureRegion> meleeFrames = new Array<>();
		for (int i = 0; i < 15; i++) {
			meleeFrames.add(new TextureRegion(assets.get("entity/player/shotgun/melee/Player_Shotgun_Melee_" + i + ".png", Texture.class)));
		}
		meleeAnimation = new Animation<>(0.035f, meleeFrames, Animation.PlayMode.NORMAL);


		Array<TextureRegion> moveFrames = new Array<>();
		for (int i = 0; i < 20; i++) {
			moveFrames.add(new TextureRegion(assets.get("entity/player/shotgun/move/Player_Shotgun_Move_" + i + ".png", Texture.class)));
		}
		moveAnimation = new Animation<>(0.1f, moveFrames, Animation.PlayMode.LOOP);


		Array<TextureRegion> reloadFrames = new Array<>();
		for (int i = 0; i < 15; i++) {
			reloadFrames.add(new TextureRegion(assets.get("entity/player/shotgun/reload/Player_Shotgun_Reload_" + i + ".png", Texture.class)));
		}
		reloadAnimation = new Animation<>(0.06f, reloadFrames, Animation.PlayMode.NORMAL);


		attackSound = assets.get("sound/sfx/weapon/shotgun/Shotgun_Shot.wav", Sound.class);
		reloadSound = assets.get("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

}
