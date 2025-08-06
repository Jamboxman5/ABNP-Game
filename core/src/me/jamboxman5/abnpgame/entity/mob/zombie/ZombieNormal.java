package me.jamboxman5.abnpgame.entity.mob.zombie;

import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class ZombieNormal extends Zombie {

    public ZombieNormal(ABNPGame game, Vector2 startPos) {
        super(game, ZombieType.NORMAL, startPos, 6, 100, 500, 50, 10,.1);
    }
}
