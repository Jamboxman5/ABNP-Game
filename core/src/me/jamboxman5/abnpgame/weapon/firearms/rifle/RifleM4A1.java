package me.jamboxman5.abnpgame.weapon.firearms.rifle;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.projectile.ammo.StandardAmmo;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class RifleM4A1 extends Firearm {

	public RifleM4A1() {
		this(new WeaponModLoadout(), new StandardAmmo(), 30);
	}
	
	public RifleM4A1(WeaponModLoadout mods, Ammo ammo, int loadedAmmo) {
		idleSprites = new Array<>(new Sprite[]{setup("entity/player/rifle/Player_Rifle", null)});
		shootSprites = new Array<>(new Sprite[]{setup("entity/player/rifle/Player_Rifle_Shoot_2", null),
									setup("entity/player/rifle/Player_Rifle_Shoot_1", null),
									setup("entity/player/rifle/Player_Rifle_Shoot_0", null),
									setup("entity/player/rifle/Player_Rifle_Shoot_0", null),
									setup("entity/player/rifle/Player_Rifle_Shoot_0", null)});
		reloadSprites = new Array<>(new Sprite[]{setup("entity/player/rifle/Player_Rifle", null)});
		this.attackSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/rifle/Assault_Rifle_Shot.wav"));
		this.reloadSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/rifle/Assault_Rifle_Reload.wav"));
		this.attackRateMS = 86;
		this.damage = 30;
		this.equippedMods = mods;
		this.reloadSpeedMS = 2300;
		this.magSize = 30;
		this.range = 1000;
		this.activeSprites = idleSprites;
		this.hudSprite = setup("weapon/rifle/M4A1", .35f);
		this.loaded = loadedAmmo;
		this.currentAmmo = ammo;
		this.name = "M4A1";
		this.firingVelocity = 150;
		this.type = WeaponType.M4A1;
		this.yOffset = 18;
	}

}
