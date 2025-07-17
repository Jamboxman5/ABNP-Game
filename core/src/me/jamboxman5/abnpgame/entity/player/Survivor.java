package me.jamboxman5.abnpgame.entity.player;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.entity.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.mods.RedDotSight;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public abstract class Survivor extends Mob {

    protected WeaponLoadout weapons;

    protected Vector2 aimTarget;
    protected float rotationSpeed;

    public Survivor(ABNPGame gamePanel, String type, Vector2 startPos, int health, int maxHealth, int speed) {
        super(gamePanel, type, startPos, health, maxHealth, speed);

        WeaponModLoadout mods = new WeaponModLoadout();
        mods.addMod(new RedDotSight());
        weapons = new WeaponLoadout();
        weapons.getActiveWeapon().setMods(mods);
        aimTarget = new Vector2();

    }

    protected void drawRedDotSight(ShapeRenderer shape, Vector2 start, Vector2 end) {

        end.rotateAroundDeg(start, jitter);

        shape.setColor(.8f, 0f, 0f, .5f);
        shape.rectLine(start, end, 2);
        shape.circle(end.x, end.y, 3, 4);
        shape.setColor((float) (255.0/255.0), (float) (200.0/255.0), (float) (200.0/255.0), 1f);
        shape.circle(end.x, end.y, 1, 4);

    }

    public void setAnimFrame(int i) {
        animFrame = i;
    }

    public double getAimAngle() {
        return getAngleToPoint(aimTarget);
    }
    public Vector2 getAimVector() { return aimTarget; }
}
