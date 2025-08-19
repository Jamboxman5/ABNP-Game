package me.jamboxman5.abnpgame.weapon.firearms.rifle;



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

public class RifleAK47 extends Firearm {

	public static Animation<TextureRegion> idleAnimation;
	public static Animation<TextureRegion> shootAnimation;
	public static Animation<TextureRegion> reloadAnimation;
	public static Animation<TextureRegion> meleeAnimation;
	public static Animation<TextureRegion> moveAnimation;

	public static Sprite hudSprite;

	public static Sound attackSound;
	public static Sound reloadSound;

	public RifleAK47() {
		this(new WeaponModLoadout(), new StandardAmmo(), 30);
	}

	public RifleAK47(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {

		super.shootAnimation = shootAnimation;
		super.reloadAnimation = reloadAnimation;
		super.idleAnimation = idleAnimation;
		super.moveAnimation = moveAnimation;
		super.meleeAnimation = meleeAnimation;

		super.hudSprite = hudSprite;

		super.attackSound = attackSound;
		super.reloadSound = reloadSound;

		this.attackRateMS = 100;
		this.damage = 60;
		this.equippedMods = mods;
		this.reloadSpeedMS = 2300;
		this.magSize = 30;
		this.range = 1000;
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "AK-47";
		this.firingVelocity = 150;
		this.type = me.jamboxman5.abnpgame.weapon.WeaponType.AK47;
		this.xOffset = 0;
		this.yOffset = 12;
		this.shootXOffset = 8;
		this.shootYOffset = 12;
		this.meleeXOffset = 0;
		this.meleeYOffset = 14;
		this.recoil = 5.5;

		currentAnimation = super.idleAnimation;

	}

	public static void loadAssets(AssetManager assets) {
		// Sprites (Textures)
		assets.load("weapon/rifle/AK47.png", Texture.class);

		assets.load("entity/player/rifle/shoot/Player_Rifle_Shoot_2.png", Texture.class);
		assets.load("entity/player/rifle/shoot/Player_Rifle_Shoot_1.png", Texture.class);
		assets.load("entity/player/rifle/shoot/Player_Rifle_Shoot_0.png", Texture.class);

		for (int i = 0; i < 20; i++) {
			assets.load("entity/player/rifle/idle/Player_Rifle_Idle_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 15; i++) {
			assets.load("entity/player/rifle/melee/Player_Rifle_Melee_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 20; i++) {
			assets.load("entity/player/rifle/move/Player_Rifle_Move_" + i + ".png", Texture.class);
		}

		for (int i = 0; i < 20; i++) {
			assets.load("entity/player/rifle/reload/Player_Rifle_Reload_" + i + ".png", Texture.class);
		}

		assets.load("sound/sfx/weapon/rifle/Rifle_AK47_Shot.wav", Sound.class);
		assets.load("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

	public static void loadSprites(AssetManager assets) {
		hudSprite = setup("weapon/rifle/AK47.png", assets, .15f);

		Array<TextureRegion> shootFrames = new Array<>();
		for (int i = 0; i < 3; i++) {
			shootFrames.add(new TextureRegion(assets.get("entity/player/rifle/shoot/Player_Rifle_Shoot_" + i + ".png", Texture.class)));
		}
		shootAnimation = new Animation<>(0.05f, shootFrames, Animation.PlayMode.NORMAL);


		Array<TextureRegion> idleFrames = new Array<>();
		for (int i = 0; i < 20; i++) {
			idleFrames.add(new TextureRegion(assets.get("entity/player/rifle/idle/Player_Rifle_Idle_" + i + ".png", Texture.class)));
		}
		idleAnimation = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);


		Array<TextureRegion> meleeFrames = new Array<>();
		for (int i = 0; i < 15; i++) {
			meleeFrames.add(new TextureRegion(assets.get("entity/player/rifle/melee/Player_Rifle_Melee_" + i + ".png", Texture.class)));
		}
		meleeAnimation = new Animation<>(0.035f, meleeFrames, Animation.PlayMode.NORMAL);


		Array<TextureRegion> moveFrames = new Array<>();
		for (int i = 0; i < 20; i++) {
			moveFrames.add(new TextureRegion(assets.get("entity/player/rifle/move/Player_Rifle_Move_" + i + ".png", Texture.class)));
		}
		moveAnimation = new Animation<>(0.1f, moveFrames, Animation.PlayMode.LOOP);


		Array<TextureRegion> reloadFrames = new Array<>();
		for (int i = 0; i < 20; i++) {
			reloadFrames.add(new TextureRegion(assets.get("entity/player/rifle/reload/Player_Rifle_Reload_" + i + ".png", Texture.class)));
		}
		reloadAnimation = new Animation<>(0.06f, reloadFrames, Animation.PlayMode.NORMAL);

		attackSound = assets.get("sound/sfx/weapon/rifle/Rifle_AK47_Shot.wav", Sound.class);
		reloadSound = assets.get("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

}
