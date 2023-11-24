package me.jamboxman5.abnpgame.entity.projectile.ammo;

public class StandardAmmo extends Ammo {
	
	public StandardAmmo(int ammoCount) {
		this.ammoCount = ammoCount;
		this.damageBoost = 1;
		this.speedBoost = 1;
		this.spread = .3;
		this.rangeBoost = 1;
		this.shots = 1;
		this.type = AmmoType.StandardAmmo;
	}
	
	public StandardAmmo() { this(100); }
	
	
}
