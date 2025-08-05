package me.jamboxman5.abnpgame.entity.zombie;

import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class ZombieTank extends Zombie{
    public ZombieTank(ABNPGame game, Vector2 startPos) {
        super(game, ZombieType.TANK, startPos, 2, 300, .5);
    }
}
