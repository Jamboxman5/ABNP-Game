package me.jamboxman5.abnpgame.entity.mob.npc;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class Ally extends Survivor {

    protected Vector3 desiredTarget;

    public Ally(ABNPGame gamePanel, String name) {
        super(gamePanel, name, new Vector3(), 100, 100, 4);
        rotationSpeed = 180f;
    }

    protected boolean canShoot() {

        Vector2 currentDir = new Vector2(aimTarget.x, aimTarget.z).sub(position.x, position.z).nor();
        Vector2 targetDir = new Vector2(desiredTarget.x, desiredTarget.z).sub(position.z, position.z).nor();

        float angleBetween = currentDir.angleDeg(targetDir);

        return (angleBetween < 2f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        getCollision().setPosition(new Vector2(position.x, position.z+10).rotateAroundDeg(new Vector2(position.x, position.z), (float) (Math.toDegrees(getAngleToPoint(target)) + 360)));

        target = gp.getPlayer().getPosition();

        if (weapons.getActiveWeapon() instanceof Firearm) {
            if (weapons.getActiveFirearm().isEmpty()) {
                weapons.nextWeapon();
            }
        }

        if (gp.getMapManager().getNearestZombie(position) != null) {
            desiredTarget = gp.getMapManager().getNearestZombie(position).getPosition();
            if (distanceTo(aimTarget) > ((Firearm) weapons.getActiveWeapon()).getRange()) desiredTarget = gp.getPlayer().getPosition();
            else {

                if (canShoot()) {
                    if (weapons.getActiveWeapon().attack(this, Math.toRadians(jitter))) {

                        jitter = (float) (Math.random() * weapons.getActiveFirearm().getRecoil());
                        if (Math.random() > .5) jitter = -jitter;

                    }
                }


            }
        } else {
            desiredTarget = gp.getPlayer().getPosition();
        }

        updateAim(delta);

        arrive(new Vector3(gp.getPlayer().getCollision().x, gp.getPlayer().getPosition().y, gp.getPlayer().getCollision().y), 300, 100);

    }

    public void updateAim(float delta) {
        // Direction vectors from position to targets
        Vector2 currentDir = new Vector2(aimTarget.x, aimTarget.z).sub(position.x, position.z).nor();
        Vector2 targetDir = new Vector2(desiredTarget.x, desiredTarget.z).sub(position.z, position.z).nor();

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
            rotated.scl(currentDistance);
            aimTarget.set(position).add(rotated.x, 0, rotated.y);
        }
    }

    @Override
    public void draw(DecalBatch batch, ShapeRenderer shape, PerspectiveCamera camera) {
        // Make sure the batch follows the camera

        TextureRegion weaponFrame = weapons.getActiveWeapon().getPlayerSprite(animFrame);

        float angleDeg;
        if (aimTarget != null) {
            angleDeg = (float) Math.toDegrees(getAngleToPoint(aimTarget)) + jitter;
        } else {
            angleDeg = (float) Math.toDegrees(getAngleToPoint(target)) + jitter;
        }

        bodyDecal.setTextureRegion(weaponFrame);
        bodyDecal.setWidth(weaponFrame.getRegionWidth());
        bodyDecal.setHeight(weaponFrame.getRegionHeight());

        // 🔹 Update legs animation frame
        TextureRegion legFrame = currentAnimation.getKeyFrame(animFrame);
        legsDecal.setTextureRegion(legFrame);
        legsDecal.setWidth(legFrame.getRegionWidth());
        legsDecal.setHeight(legFrame.getRegionHeight());

        legsDecal.setPosition(new Vector3(position.x, position.y, -position.z));
        bodyDecal.setPosition(new Vector3(position.x, position.y, -position.z));
        legsDecal.translateY(2);
        bodyDecal.translateY(4);

// Face the camera
        legsDecal.lookAt(camera.position, camera.up);
        bodyDecal.lookAt(camera.position, camera.up);
//		System.out.println(camera.up);

// Rotate around Y for facing direction
        legsDecal.rotateZ(angleDeg);
        bodyDecal.rotateZ(angleDeg);

        legsDecal.setScale(.25f);
        bodyDecal.setScale(.25f);

        if (weapons.getActiveWeapon().isShootAnimation()) {
            bodyDecal.translateX((bodyDecal.getWidth() * .1f)/2f + weapons.getActiveWeapon().getShootXOffset());
            bodyDecal.translateZ((bodyDecal.getHeight() * .1f)/2f + weapons.getActiveWeapon().getShootYOffset());
        } else if (weapons.getActiveWeapon().isMeleeAnimation()) {
            bodyDecal.translateX((bodyDecal.getWidth() * .1f)/2f + weapons.getActiveWeapon().getMeleeXOffset());
            bodyDecal.translateZ((bodyDecal.getHeight() * .1f)/2f + weapons.getActiveWeapon().getMeleeYOffset());
        } else {
            bodyDecal.translateX((bodyDecal.getWidth() * .1f)/2f + weapons.getActiveWeapon().getXOffset());
            bodyDecal.translateZ((bodyDecal.getHeight() * .1f)/2f + weapons.getActiveWeapon().getYOffset());
        }
        if (currentAnimation == strafeBackAnimation) {
            legsDecal.translateX(-10);
        }

        batch.add(legsDecal);
        batch.add(bodyDecal);
    }


    @Override
    public boolean hasCollided(double xComp, double yComp) {
        return false;
    }
}
