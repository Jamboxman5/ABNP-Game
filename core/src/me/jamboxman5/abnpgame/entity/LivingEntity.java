package me.jamboxman5.abnpgame.entity;

import me.jamboxman5.abnpgame.main.ABNPGame;

public abstract class LivingEntity extends Entity {

    protected int health;
    protected int maxHealth;

    public LivingEntity(ABNPGame gamePanel, int health, int maxHealth) {
        super(gamePanel);
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public boolean isDead() { return health <= 0; }
    public void kill() { health = 0; }
    public void damage(int dmgPts) { health -= dmgPts; }
    public void heal() { health = maxHealth; }
    public void healBy(int healPts, boolean ignoreMax) {
        health += healPts;
        if (!ignoreMax && health > maxHealth) {
            health = maxHealth;
        }
    }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public float getHealthRatio() { return ((float)health)/((float)maxHealth); }
}
