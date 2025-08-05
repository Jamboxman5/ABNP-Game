package me.jamboxman5.abnpgame.entity.zombie;

import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class ZombieRunner extends Zombie {

    public ZombieRunner(ABNPGame game, Vector2 startPos) {
        super(game, ZombieType.RUNNER, startPos, 10, 50, .05);
    }

}
