package me.jamboxman5.abnpgame.entity.mob.npc;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class Ally extends Survivor {

    protected Vector2 desiredTarget;

    public Ally(ABNPGame gamePanel, String type, Vector2 startPos, int health, int maxHealth, int speed) {
        super(gamePanel, type, startPos, health, maxHealth, speed);
        target = gp.getPlayer().getPosition();
        rotationSpeed = 180f;
    }

    protected boolean canShoot() {

        Vector2 currentDir = new Vector2(aimTarget).sub(position).nor();
        Vector2 targetDir = new Vector2(desiredTarget).sub(position).nor();

        float angleBetween = currentDir.angleDeg(targetDir);

        return (angleBetween < 2f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        getCollision().setPosition(new Vector2(position.x, position.y+10).rotateAroundDeg(position, (float) (Math.toDegrees(getAngleToPoint(target)) + 360)));

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
            desiredTarget = gp.getMapManager().getNearestZombie(position).getPosition();
            if (distanceTo(aimTarget) > ((Firearm) weapons.getActiveWeapon()).getRange()) desiredTarget = gp.getPlayer().getPosition();
            else {

                if (canShoot()) {
                    if (weapons.getActiveWeapon().attack(this, Math.toRadians(jitter))) {

                        jitter = (float) (Math.random() * weapons.getActiveWeapon().getRecoil());
                        if (Math.random() > .5) jitter = -jitter;

                    }
                }


            }
        } else {
            desiredTarget = gp.getPlayer().getPosition();
        }

        updateAim(delta);

        arrive(new Vector2(gp.getPlayer().getCollision().x, gp.getPlayer().getCollision().y), 300, 100);

    }

    public void updateAim(float delta) {
        // Direction vectors from position to targets
        Vector2 currentDir = new Vector2(aimTarget).sub(position).nor();
        Vector2 targetDir = new Vector2(desiredTarget).sub(position).nor();

        float angleBetween = currentDir.angleDeg(targetDir);
        float crossZ = currentDir.crs(targetDir); // Sign for direction

        float maxRotate = rotationSpeed * delta;

        if (angleBetween < 0.1f) {
            // Close enough — snap
            aimTarget.set(desiredTarget);
        } else {
            float rotation = Math.min(angleBetween, maxRotate);
            Vector2 rotated = new Vector2(currentDir).rotateDeg(rotation * Math.signum(crossZ));

            // Set the new realAimTarget based on rotated direction (keep same distance)
            float currentDistance = aimTarget.dst(position);
            aimTarget.set(position).add(rotated.scl(currentDistance));
        }
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
