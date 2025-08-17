package me.jamboxman5.abnpgame.weapon.firearms.rifle;



import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class RifleAK47 extends Firearm {

	public static Array<Sprite> idleSprites;
	public static Array<Sprite> shootSprites;
	public static Array<Sprite> reloadSprites;
	public static Sprite hudSprite;

	public static Sound attackSound;
	public static Sound reloadSound;

	public RifleAK47() {
		this(new WeaponModLoadout(), new StandardAmmo(), 30);
	}

	public RifleAK47(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {

		super.shootSprites = shootSprites;
		super.reloadSprites = reloadSprites;
		super.idleSprites = idleSprites;
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
		this.xOffset = 18;
		this.yOffset = 12;
		this.recoil = 3.5;

		activeSprites = super.idleSprites;

	}

	public static void loadAssets(AssetManager assets) {
		// Sprites (Textures)
		assets.load("weapon/rifle/AK47.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle_Shoot_2.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle_Shoot_1.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle_Shoot_0.png", Texture.class);

		// Sounds
		assets.load("sound/sfx/weapon/rifle/Rifle_AK47_Shot.wav", Sound.class);
		assets.load("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

	public static void loadSprites(AssetManager assets) {
		hudSprite = setup("weapon/rifle/AK47.png", assets, .15f);

		idleSprites = new Array<>(new Sprite[]{
				setup("entity/player/rifle/Player_Rifle.png", assets, .25f)
		});

		shootSprites = new Array<>(new Sprite[]{
				setup("entity/player/rifle/Player_Rifle_Shoot_2.png", assets, .25f),
				setup("entity/player/rifle/Player_Rifle_Shoot_2.png", assets, .25f),
				setup("entity/player/rifle/Player_Rifle_Shoot_1.png", assets, .25f),
				setup("entity/player/rifle/Player_Rifle_Shoot_1.png", assets, .25f),
				setup("entity/player/rifle/Player_Rifle_Shoot_0.png", assets, .25f),
				setup("entity/player/rifle/Player_Rifle_Shoot_0.png", assets, .25f),
				setup("entity/player/rifle/Player_Rifle_Shoot_0.png", assets, .25f)
		});

		reloadSprites = new Array<>(new Sprite[]{
				setup("entity/player/rifle/Player_Rifle.png", assets, .25f)
		});

		attackSound = assets.get("sound/sfx/weapon/rifle/Rifle_AK47_Shot.wav", Sound.class);
		reloadSound = assets.get("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

}
