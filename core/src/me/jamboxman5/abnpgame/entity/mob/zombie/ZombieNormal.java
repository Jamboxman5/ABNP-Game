package me.jamboxman5.abnpgame.entity.mob.zombie;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class ZombieNormal extends Zombie {

    public ZombieNormal(ABNPGame game, Vector3 startPos) {
        super(game, ZombieType.NORMAL, startPos, 3, 100, 500, 50, 10,5, PursuitType.ARRIVE);
    }
}
