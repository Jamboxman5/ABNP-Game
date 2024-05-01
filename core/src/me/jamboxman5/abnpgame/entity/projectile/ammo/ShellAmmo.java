package me.jamboxman5.abnpgame.entity.projectile.ammo;

import com.badlogic.gdx.Gdx;

public class ShellAmmo extends Ammo {
	
	public ShellAmmo(int ammoCount) {
		this.ammoCount = ammoCount;
		this.damageBoost = 1;
		this.speedBoost = 1;
		this.spread = 1;
		this.rangeBoost = 1;
		this.shots = 5;
		this.type = AmmoType.ShellAmmo;
		this.impactSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/weapon/misc/Bullet_Impact_Flesh.wav"));
		this.breachCount = 3;
	}
	
	public ShellAmmo() { this(100); }
	
	
}
