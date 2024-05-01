package me.jamboxman5.abnpgame.entity;

import me.jamboxman5.abnpgame.main.ABNPGame;

public abstract class LivingEntity extends Entity {

    protected double health;
    protected double maxHealth;

    public LivingEntity(ABNPGame gamePanel, int health, int maxHealth) {
        super(gamePanel);
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public boolean isDead() { return health <= 0; }
    public void kill() { health = 0; }
    public void damage(double dmgPts) {
        health -= dmgPts;
        if (health < 0) health = 0;

    }
    public void heal() { health = maxHealth; }
    public void healBy(double healPts, boolean ignoreMax) {
        health += healPts;
        if (!ignoreMax && health > maxHealth) {
            health = maxHealth;
        }
    }
    public double getHealth() { return health; }
    public double getMaxHealth() { return maxHealth; }
    public float getHealthRatio() { return ((float)health)/((float)maxHealth); }
}
