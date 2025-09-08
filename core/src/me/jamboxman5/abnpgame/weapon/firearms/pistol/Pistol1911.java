package me.jamboxman5.abnpgame.weapon.firearms.pistol;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.util.UUID;

public class Pistol1911 extends Firearm {

	public static Animation<TextureRegion> idleAnimation;
	public static Animation<TextureRegion> shootAnimation;
	public static Animation<TextureRegion> reloadAnimation;
	public static Animation<TextureRegion> meleeAnimation;
	public static Animation<TextureRegion> moveAnimation;

	public static Sprite hudSprite;

	public static Sound attackSound;
	public static Sound reloadSound;
	public static Sound silencerSound;

	public Pistol1911() {
		this(new WeaponModLoadout(), new StandardAmmo(), 8, UUID.randomUUID().toString());
	}
	
	public Pistol1911(WeaponModLoadout mods, Ammo ammo, int loadedAmmo, String id) {

		super.weaponID = id;

		super.shootAnimation = shootAnimation;
		super.reloadAnimation = reloadAnimation;
		super.idleAnimation = idleAnimation;
		super.moveAnimation = moveAnimation;
		super.meleeAnimation = meleeAnimation;

		super.hudSprite = hudSprite;

		super.attackSound = attackSound;
		super.reloadSound = reloadSound;
		super.silencerSound = silencerSound;

		this.attackRateMS = 350;
		this.damage = 18;
		this.equippedMods = mods;
		this.reloadSpeedMS = 1500;
		this.magSize = 8;
		this.range = 480;
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "M1911";
		this.firingVelocity = 150;
		this.type = me.jamboxman5.abnpgame.weapon.WeaponType.M1911;
		this.xOffset = 0;
		this.yOffset = 14;
		this.shootXOffset = 15;
		this.shootYOffset = 15;
		this.meleeXOffset = 3;
		this.meleeYOffset = 10;
		this.recoil = 2;

		super.currentAnimation = super.idleAnimation;


	}

	public static void loadAssets(AssetManager assets) {
		// Sprites (Textures)
		assets.load("weapon/pistol/1911.png", Texture.class);

		assets.load("entity/player/handgun/shoot/Player_Handgun_Shoot_2.png", Texture.class);
		assets.load("entity/player/handgun/shoot/Player_Handgun_Shoot_1.png", Texture.class);
		assets.load("entity/player/handgun/shoot/Player_Handgun_Shoot_0.png", Texture.class);

		for (int i = 0; i < 20; i++) {
			assets.load("entity/player/handgun/idle/Player_Handgun_Idle_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 15; i++) {
			assets.load("entity/player/handgun/melee/Player_Handgun_Melee_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 20; i++) {
			assets.load("entity/player/handgun/move/Player_Handgun_Move_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 15; i++) {
			assets.load("entity/player/handgun/reload/Player_Handgun_Reload_" + i + ".png", Texture.class);
		}

		// Sounds
		assets.load("sound/sfx/weapon/pistol/Pistol_M1911_Shot.wav", Sound.class);
		assets.load("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
		assets.load("sound/sfx/weapon/pistol/Pistol_Shot_Silencer.wav", Sound.class);
	}

	public static void loadSprites(AssetManager assets) {
		hudSprite = setup("weapon/pistol/1911.png", assets, .1f);

		Array<TextureRegion> shootFrames = new Array<>();
		for (int i = 0; i < 3; i++) {
			shootFrames.add(new TextureRegion(assets.get("entity/player/handgun/shoot/Player_Handgun_Shoot_" + i + ".png", Texture.class)));
		}
		shootAnimation = new Animation<>(0.05f, shootFrames, Animation.PlayMode.NORMAL);


		Array<TextureRegion> idleFrames = new Array<>();
		for (int i = 0; i < 20; i++) {
			idleFrames.add(new TextureRegion(assets.get("entity/player/handgun/idle/Player_Handgun_Idle_" + i + ".png", Texture.class)));
		}
		idleAnimation = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);


		Array<TextureRegion> meleeFrames = new Array<>();
		for (int i = 0; i < 15; i++) {
			meleeFrames.add(new TextureRegion(assets.get("entity/player/handgun/melee/Player_Handgun_Melee_" + i + ".png", Texture.class)));
		}
		meleeAnimation = new Animation<>(0.035f, meleeFrames, Animation.PlayMode.NORMAL);


		Array<TextureRegion> moveFrames = new Array<>();
		for (int i = 0; i < 20; i++) {
			moveFrames.add(new TextureRegion(assets.get("entity/player/handgun/move/Player_Handgun_Move_" + i + ".png", Texture.class)));
		}
		moveAnimation = new Animation<>(0.1f, moveFrames, Animation.PlayMode.LOOP);


		Array<TextureRegion> reloadFrames = new Array<>();
		for (int i = 0; i < 15; i++) {
			reloadFrames.add(new TextureRegion(assets.get("entity/player/handgun/reload/Player_Handgun_Reload_" + i + ".png", Texture.class)));
		}
		reloadAnimation = new Animation<>(0.06f, reloadFrames, Animation.PlayMode.NORMAL);


		attackSound = assets.get("sound/sfx/weapon/pistol/Pistol_M1911_Shot.wav", Sound.class);
		reloadSound = assets.get("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
		silencerSound = assets.get("sound/sfx/weapon/pistol/Pistol_Shot_Silencer.wav", Sound.class);
	}
}
