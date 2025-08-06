package me.jamboxman5.abnpgame.entity.mob.zombie;

import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class ZombieTank extends Zombie{
    public ZombieTank(ABNPGame game, Vector2 startPos) {
        super(game, ZombieType.TANK, startPos, 0, 300, 800, 200, 50, .5);
    }
}
