package me.jamboxman5.abnpgame.entity.projectile.ammo;

import com.badlogic.gdx.Gdx;

public class SlugAmmo extends Ammo {

	public SlugAmmo(int ammoCount) {
		this.ammoCount = ammoCount;
		this.damageBoost = 40;
		this.speedBoost = 1;
		this.spread = 1;
		this.rangeBoost = 1;
		this.shots = 1;
		this.type = AmmoType.SlugAmmo;
		this.impactSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/misc/Bullet_Impact_Flesh.wav"));
		this.breachCount = 5;
	}

	public SlugAmmo() { this(100); }
	
	
}
