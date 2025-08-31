package me.jamboxman5.abnpgame.weapon;



import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.PistolTec9;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleAK47;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM1Garand;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunAssault;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import java.util.UUID;

public abstract class Weapon {

	protected double damage;
	protected double durability;
	protected double weight;

	
	protected long attackRateMS;
	protected long lastAttack;

	protected Animation<TextureRegion> idleAnimation;
	protected Animation<TextureRegion> shootAnimation;
	protected Animation<TextureRegion> reloadAnimation;
	protected Animation<TextureRegion> meleeAnimation;
	protected Animation<TextureRegion> moveAnimation;

	protected Animation<TextureRegion> currentAnimation;

	protected Sprite dropSprite;
	protected Sprite hudSprite;
	protected Sound attackSound;
	protected String name;
	protected WeaponType type;
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected int shootXOffset = 0;
	protected int shootYOffset = 0;
	protected int meleeXOffset = 0;
	protected int meleeYOffset = 0;
	protected final static float playerSpriteScale = .25f;

	protected WeaponModLoadout equippedMods;

	protected String weaponID;

	public void setID(String uuid) { weaponID = uuid; }
	public String getID(String uuid) { return weaponID; }

	public Sprite getPlayerSprite(float stateTime) {
		Sprite toDraw = new Sprite(currentAnimation.getKeyFrame(stateTime));
		toDraw.setScale(playerSpriteScale);
		return toDraw;
	}
	public Sprite getHudSprite() { return hudSprite; }
	public String getName() { return name; }
	public abstract boolean attack(Survivor attacker, double radians);
	public abstract boolean fakeAttack(Survivor attacker);
	protected boolean canAttack() {
	    if ((System.currentTimeMillis() - lastAttack) < attackRateMS) return false;
		return true;
	}
	public boolean hasMod(WeaponMod.ModType type) {
		return equippedMods.hasMod(type);
	}
	public void idle() { currentAnimation = idleAnimation; }
	public void move() { currentAnimation = moveAnimation; }
	public void setMods(WeaponModLoadout mods) {
		equippedMods = mods;
	}

	public WeaponModLoadout getModLoadout() { return equippedMods; }

	public int getXOffset() { return xOffset; }

	public float getYOffset() { return yOffset; }
	public int getShootXOffset() { return shootXOffset; }

	public float getShootYOffset() { return shootYOffset; }
	public int getMeleeXOffset() { return meleeXOffset; }

	public float getMeleeYOffset() { return meleeYOffset; }


	public WeaponType getType() { return type; }
	public static Weapon getByType(WeaponType type) {
		switch(type) {
		case M1911:
			return new Pistol1911();
		case M4A1:
			return new RifleM4A1();
		case AK47:
			return new RifleAK47();
		case M1GARAND:
			return new RifleM1Garand();
		case WINCHESTER12:
			return new ShotgunWinchester12();
		case ASSAULTSHOTGUN:
			return new ShotgunAssault();
		case TEC9:
			return new PistolTec9();
		}
		return new RifleM4A1();
	}

	protected static Sprite setup(String imagePath, AssetManager assets, Float scale) {

		Sprite s = new Sprite(assets.get(imagePath, Texture.class));
		if (scale == null) {
			s.setScale(playerSpriteScale);
		} else {
			s.setScale(scale);
		}
		return s;
//        BufferedImage image = null;
//
//        try {
//        	InputStream src = getClass().getResourceAsStream("/me/jamboxman5/abnpgame" + imagePath + ".png");
//            image = ImageIO.read(src);
//
//        } catch (IOException | IllegalArgumentException e) {
//            e.printStackTrace();
//            System.out.println(imagePath);
//        }
//
//        return Utilities.scaleImage(image, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale));
	}

	public double getDamage() { return damage; }
	public Animation<TextureRegion> getCurrentAnimation() { return currentAnimation; }
	public boolean isShootAnimation() { return currentAnimation == shootAnimation; }
	public boolean isMeleeAnimation() { return currentAnimation == meleeAnimation; }

	public Vector2 getOffset() {
		Vector2 offset = new Vector2();
		if (isShootAnimation()) {
			offset.x = getShootXOffset();
			offset.y = getShootYOffset();
		} else if (isMeleeAnimation()) {
			offset.x = getMeleeXOffset();
			offset.y = getMeleeYOffset();
		} else {
			offset.x = getXOffset();
			offset.y = getYOffset();
		}
		return offset;
	}

}
