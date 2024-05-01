package me.jamboxman5.abnpgame.entity.projectile.ammo;

import com.badlogic.gdx.Gdx;

public class StandardAmmo extends Ammo {
	
	public StandardAmmo(int ammoCount) {
		this.ammoCount = ammoCount;
		this.damageBoost = 1;
		this.speedBoost = 1;
		this.spread = .3;
		this.rangeBoost = 1;
		this.shots = 1;
		this.type = AmmoType.StandardAmmo;
		this.impactSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/misc/Bullet_Impact_Flesh.wav"));
		this.breachCount = 1;
	}
	
	public StandardAmmo() { this(100); }
	
	
}
