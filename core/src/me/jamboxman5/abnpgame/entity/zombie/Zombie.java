package me.jamboxman5.abnpgame.entity.zombie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class Zombie extends Mob {

    public Zombie(ABNPGame game, ZombieType type, Vector2 startPos, int topSpeed) {
        super(game,
                type.toString(),
                (int) startPos.x,
                (int) startPos.y,
                20, 20,
                topSpeed);

    }

    @Override
    public void update() {
        pursue(gp.getPlayer());
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {

    }

    @Override
    public boolean hasCollided(double xComp, double yComp) {
        return false;
    }

    public enum ZombieType {
        NORMAL
    }

}
