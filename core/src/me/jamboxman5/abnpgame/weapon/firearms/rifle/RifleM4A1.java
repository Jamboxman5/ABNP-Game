package me.jamboxman5.abnpgame.weapon.firearms.rifle;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class RifleM4A1 extends Firearm {

	public static Array<Sprite> idleSprites;
	public static Array<Sprite> shootSprites;
	public static Array<Sprite> reloadSprites;
	public static Sprite hudSprite;

	public static Sound attackSound;
	public static Sound reloadSound;

	public RifleM4A1() {
		this(new WeaponModLoadout(), new StandardAmmo(), 30);
	}
	
	public RifleM4A1(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {

		super.shootSprites = shootSprites;
		super.reloadSprites = reloadSprites;
		super.idleSprites = idleSprites;
		super.hudSprite = hudSprite;

		super.attackSound = attackSound;
		super.reloadSound = reloadSound;

		this.attackRateMS = 86;
		this.damage = 30;
		this.equippedMods = mods;
		this.reloadSpeedMS = 2300;
		this.magSize = 30;
		this.range = 1000;
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "M4A1";
		this.firingVelocity = 150;
		this.type = me.jamboxman5.abnpgame.weapon.WeaponType.M4A1;
		this.xOffset = 18;
		this.yOffset = 12;
		this.recoil = 1;

		activeSprites = super.idleSprites;

	}

	public static void loadAssets(AssetManager assets) {
		// Sprites (Textures)
		assets.load("weapon/rifle/M4A1.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle_Shoot_2.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle_Shoot_1.png", Texture.class);
		assets.load("entity/player/rifle/Player_Rifle_Shoot_0.png", Texture.class);

		// Sounds
		assets.load("sound/sfx/weapon/rifle/Assault_Rifle_Shot.wav", Sound.class);
		assets.load("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

	public static void loadSprites(AssetManager assets) {
		hudSprite = setup("weapon/rifle/M4A1.png", assets, .35f);

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

		attackSound = assets.get("sound/sfx/weapon/rifle/Assault_Rifle_Shot.wav", Sound.class);
		reloadSound = assets.get("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav", Sound.class);
	}

}
