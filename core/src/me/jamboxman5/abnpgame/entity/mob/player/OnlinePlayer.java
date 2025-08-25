package me.jamboxman5.abnpgame.entity.mob.player;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.PacketMove;
import me.jamboxman5.abnpgame.net.packets.PacketWeaponChange;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;

public class OnlinePlayer extends Player {

    Weapon activeWeapon;

    public OnlinePlayer(ABNPGame gamePanel, String username, String uuid) {
        super(gamePanel, username, uuid);
        setPosition(new Vector3(0,0,0));
        activeWeapon = new Pistol1911();
        this.uuid = uuid;
    }

    @Override
    public void update(float delta) {


    }

    public Weapon getActiveWeapon() { return activeWeapon; }

    public void updatePos(PacketMove packet) {
        setWorldX(packet.x);
        setWorldZ(packet.y);
        setRotation(packet.rotation);
        jitter = packet.jitter;
    }

    public void updateWeapon(PacketWeaponChange packet) {
        switch(packet.type) {
            case M4A1:
                activeWeapon = new RifleM4A1();
                break;
            case M1911:
                activeWeapon = new Pistol1911();
                break;
            case WINCHESTER12:
                activeWeapon = new ShotgunWinchester12();
                break;
        }
    }

    @Override
    public void draw(DecalBatch batch, ShapeRenderer shape, PerspectiveCamera camera) {
        TextureRegion weaponFrame = weapons.getActiveWeapon().getPlayerSprite(animFrame);

        bodyDecal.setTextureRegion(weaponFrame);
        bodyDecal.setWidth(weaponFrame.getRegionWidth());
        bodyDecal.setHeight(weaponFrame.getRegionHeight());

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
        legsDecal.rotateZ(rotation);
        bodyDecal.rotateZ(rotation);

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

    public void shoot() {
        activeWeapon.fakeAttack(this);
    }
}
