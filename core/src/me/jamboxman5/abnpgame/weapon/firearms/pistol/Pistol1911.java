package me.jamboxman5.abnpgame.weapon.firearms.pistol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class Pistol1911 extends Firearm {

	public static Array<Sprite> idleSprites;
	public static Array<Sprite> shootSprites;
	public static Array<Sprite> reloadSprites;
	public static Sprite hudSprite;

	public static Sound attackSound;
	public static Sound reloadSound;

	public Pistol1911() {
		this(new WeaponModLoadout(), new StandardAmmo(), 8);
	}
	
	public Pistol1911(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {

		super.shootSprites = shootSprites;
		super.reloadSprites = reloadSprites;
		super.idleSprites = idleSprites;
		super.hudSprite = hudSprite;

		super.attackSound = attackSound;
		super.reloadSound = reloadSound;

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
		this.xOffset = 13;
		this.yOffset = 15;
		this.recoil = 2;

		activeSprites = super.idleSprites;


	}

	public static void initSprites() {
		hudSprite = setup("weapon/pistol/1911", .35f);
		idleSprites = new Array<>(new Sprite[]{setup("entity/player/handgun/Player_Handgun", null)});
		shootSprites = new Array<>(new Sprite[]{setup("entity/player/handgun/Player_Handgun_Shoot_2", null),
				setup("entity/player/handgun/Player_Handgun_Shoot_2", null),
				setup("entity/player/handgun/Player_Handgun_Shoot_1", null),
				setup("entity/player/handgun/Player_Handgun_Shoot_1", null),
				setup("entity/player/handgun/Player_Handgun_Shoot_0", null),
				setup("entity/player/handgun/Player_Handgun_Shoot_0", null),
				setup("entity/player/handgun/Player_Handgun_Shoot_0", null)});
		reloadSprites = new Array<>(new Sprite[]{setup("entity/player/handgun/Player_Handgun", null)});


	}

	public static void initSounds() {
		attackSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/pistol/Pistol_Shot.wav"));
		reloadSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav"));

	}
}
