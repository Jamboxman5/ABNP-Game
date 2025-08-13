package me.jamboxman5.abnpgame.entity.mob.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.mob.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.net.packets.PacketMove;
import me.jamboxman5.abnpgame.net.packets.PacketWeaponChange;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.WeaponType;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;

public class OnlinePlayer extends Player {

    Weapon activeWeapon;

    public OnlinePlayer(ABNPGame gamePanel, String username, String uuid) {
        super(gamePanel, username, uuid);
        setPosition(new Vector2(0,0));
        activeWeapon = new Pistol1911();
        this.uuid = uuid;
    }

    @Override
    public void update(float delta) {
        animFrame -= 1;

        if (animFrame < 0) {
            weapons.getActiveWeapon().idle();
            animFrame = weapons.getActiveWeapon().idleSprites.size-1;
        }
    }

    public Weapon getActiveWeapon() { return activeWeapon; }

    public void updatePos(PacketMove packet) {
        setWorldX(packet.x);
        setWorldY(packet.y);
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
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        int x = (int) (((position.x - gp.getPlayer().getWorldX())*.5) + gp.getPlayer().getScreenX());
        int y = (int) (((position.y - gp.getPlayer().getWorldY())*.5) + gp.getPlayer().getScreenY());

        batch.begin();
        Sprite toDraw = activeWeapon.getPlayerSprite(animFrame);

        batch.setTransformMatrix(new Matrix4().translate((float) x, (float) y, 0).rotate(0f, 0f, 1f, (float) (Math.toDegrees(getRotation()) + 360) + jitter));
        toDraw.setPosition((-toDraw.getWidth() / 2) + activeWeapon.getXOffset(), (-toDraw.getHeight() / 2) + activeWeapon.getYOffset());
        toDraw.draw(batch);
        batch.setTransformMatrix(new Matrix4());

        float nameScale = .3f;
        int nameX = (int) (x - ((x - Fonts.getXForCenteredText(x, name, Fonts.INFOFONT)) * nameScale));
        Fonts.drawScaled(Fonts.INFOFONT, nameScale, name, batch, nameX, y + 40);

        batch.end();


    }

    @Override
    public boolean hasCollided(double xComp, double yComp) {
        return false;
    }

    public void shoot() {
        activeWeapon.attack(this, Math.toRadians(jitter));
    }
}
