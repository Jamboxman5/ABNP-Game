package me.jamboxman5.abnpgame.weapon;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;
import me.jamboxman5.abnpgame.weapon.mods.RedDotSight;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public abstract class Weapon {

	protected double damage;
	protected double durability;
	protected double weight;
	protected boolean reloading = false;
	protected double recoil;
	
	protected long attackRateMS;
	protected long lastAttack;
		
	public Array<Sprite> idleSprites;
	public Array<Sprite> shootSprites;
	public Array<Sprite> reloadSprites;
	public Array<Sprite> activeSprites;
			
	protected Sprite dropSprite;
	protected Sprite hudSprite;
	protected Sound attackSound;
	protected String name;
	protected WeaponType type;
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected final static float playerSpriteScale = .25f;

	protected WeaponModLoadout equippedMods;
	
	public Sprite getPlayerSprite(int idx) {
		if (idx < activeSprites.size) return activeSprites.get(idx);
		else return activeSprites.get(0);

	}
	public Sprite getHudSprite() { return hudSprite; }
	public String getName() { return name; }
	public abstract boolean attack(Survivor attacker, double radians);
	public abstract boolean fakeAttack(Survivor attacker);
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

	public int getXOffset() { return xOffset; }

	public float getYOffset() { return yOffset; }

	public double getRecoil() { return recoil;
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

	protected static Sprite setup(String imagePath, Float scale) {
		Texture t = new Texture(Gdx.files.internal(imagePath + ".png/"));
		Sprite s = new Sprite(t);
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
}
