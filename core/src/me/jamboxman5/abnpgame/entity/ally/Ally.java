package me.jamboxman5.abnpgame.entity.ally;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.Mob;
import me.jamboxman5.abnpgame.entity.player.Player;
import me.jamboxman5.abnpgame.entity.player.Survivor;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class Ally extends Survivor {

    public Ally(ABNPGame gamePanel, String type, Vector2 startPos, int health, int maxHealth, int speed) {
        super(gamePanel, type, startPos, health, maxHealth, speed);
        target = gp.getPlayer().getPosition();
    }

    @Override
    public void update() {
        super.update();

        collision.setPosition(new Vector2(position.x, position.y+10).rotateAroundDeg(position, (float) (Math.toDegrees(getAngleToPoint(target)) + 360)));

        target = gp.getPlayer().getPosition();

        if (weapons.getActiveWeapon() instanceof Firearm) {
            if (weapons.getActiveFirearm().isEmpty()) {
                weapons.nextWeapon();
            }
        }

        animFrame -= 1;

        if (animFrame < 0) {
            weapons.getActiveWeapon().idle();
            animFrame = weapons.getActiveWeapon().idleSprites.size-1;
        }

        if (gp.getMapManager().getNearestZombie(position) != null) {
            aimTarget = gp.getMapManager().getNearestZombie(position).getPosition();
            if (distanceTo(aimTarget) > ((Firearm) weapons.getActiveWeapon()).getRange()) aimTarget = gp.getPlayer().getPosition();
            else {
                if (weapons.getActiveWeapon().attack(this, Math.toRadians(jitter))) {

                    jitter = (float) (Math.random() * weapons.getActiveWeapon().getRecoil());
                    if (Math.random() > .5) jitter = -jitter;

                }
            }
        } else {
            aimTarget = gp.getPlayer().getPosition();
        }

        arrive(new Vector2(gp.getPlayer().getCollision().x, gp.getPlayer().getCollision().y), 300, 100);

    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        int x = (int) (((position.x - gp.getPlayer().getWorldX())*.5) + gp.getPlayer().getScreenX());
        int y = (int) (((position.y - gp.getPlayer().getWorldY())*.5) + gp.getPlayer().getScreenY());

        batch.begin();
        Sprite toDraw = weapons.getActiveWeapon().getPlayerSprite(animFrame);

        if (aimTarget != null) {
            batch.setTransformMatrix(new Matrix4().translate((float) x, (float) y, 0).rotate(0f, 0f, 1f, (float) (Math.toDegrees(getAngleToPoint(aimTarget)) + 360) + jitter));
        } else {
            batch.setTransformMatrix(new Matrix4().translate((float) x, (float) y, 0).rotate(0f, 0f, 1f, (float) (Math.toDegrees(getAngleToPoint(target)) + 360) + jitter));
        }

        toDraw.setPosition((-toDraw.getWidth() / 2) + weapons.getActiveWeapon().getXOffset(), (-toDraw.getHeight() / 2) + weapons.getActiveWeapon().getYOffset());
        toDraw.draw(batch);
        batch.setTransformMatrix(new Matrix4());


        batch.end();


    }

    @Override
    public boolean hasCollided(double xComp, double yComp) {
        return false;
    }
}
