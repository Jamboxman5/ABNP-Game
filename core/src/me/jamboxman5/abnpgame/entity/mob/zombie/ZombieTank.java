package me.jamboxman5.abnpgame.entity.mob.zombie;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class ZombieTank extends Zombie{
    public ZombieTank(ABNPGame game, Vector3 startPos) {
        super(game, ZombieType.TANK, startPos, 1.5f, 300, 1200, 200, 50, 20, PursuitType.SEEK);
    }
}
