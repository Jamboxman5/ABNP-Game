package me.jamboxman5.abnpgame.entity.projectile.ammo;

public class ShellAmmo extends Ammo {
	
	public ShellAmmo(int ammoCount) {
		this.ammoCount = ammoCount;
		this.damageBoost = 1;
		this.speedBoost = 1;
		this.spread = 1;
		this.rangeBoost = 1;
		this.shots = 5;
		this.type = AmmoType.ShellAmmo;
	}
	
	public ShellAmmo() { this(100); }
	
	
}
