package me.jamboxman5.abnpgame.weapon;



import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;
import me.jamboxman5.abnpgame.weapon.mods.RedDotSight;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.awt.image.BufferedImage;

public abstract class Weapon {

	protected double damage;
	protected double durability;
	protected double weight;
	protected boolean reloading = false;
	
	protected long attackRateMS;
	protected long lastAttack;
		
	public Array<Sprite> idleSprites;
	public Array<Sprite> shootSprites;
	public Array<Sprite> reloadSprites;
	public Array<Sprite> activeSprites;
			
	protected BufferedImage dropSprite;
	protected BufferedImage hudSprite;
	protected Sound attackSound;
	protected String name;
	protected WeaponType type;
	protected int yOffset = 0;

	protected WeaponModLoadout equippedMods;
	
	public Sprite getPlayerSprite(int idx) {
		return activeSprites.get(idx);
	}
	public BufferedImage getHudSprite() { return hudSprite; }
	public String getName() { return name; }
	public abstract void attack();
	protected boolean canAttack() {
	    if (reloading) return false;
	    if ((System.currentTimeMillis() - lastAttack) < attackRateMS) return false;
		return true;
	}
	public boolean hasRedDotSight() {
		for (WeaponMod m : equippedMods.getMods()) {
			if (m instanceof RedDotSight) return true;
		}
		return false;
	}
	public void idle() { activeSprites = idleSprites; }
	public void setMods(WeaponModLoadout mods) {
		equippedMods = mods;
	}

	public WeaponModLoadout getModLoadout() { return equippedMods; }

	public int getYOffset() { return yOffset; }

	public enum WeaponType {
		M1911, M4A1, WINCHESTER12;
	}
	public WeaponType getType() { return type; }
	public static Weapon getByType(WeaponType type) {
		switch(type) {
		case M1911:
			return new Pistol1911();
		case M4A1:
			return new RifleM4A1();
		case WINCHESTER12:
			return new ShotgunWinchester12();
		}
		return new RifleM4A1();
	}
}
