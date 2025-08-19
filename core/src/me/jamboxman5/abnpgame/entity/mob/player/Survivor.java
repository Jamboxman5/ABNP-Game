package me.jamboxman5.abnpgame.entity.mob.player;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.mob.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.mods.RedDotSight;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public abstract class Survivor extends Mob {

    protected WeaponLoadout weapons;

    protected Vector2 aimTarget;
    protected float rotationSpeed;
    protected int exp;

    public static Animation<TextureRegion> idleAnimation;
    public static Animation<TextureRegion> runAnimation;
    public static Animation<TextureRegion> walkAnimation;
    public static Animation<TextureRegion> strafeLeftAnimation;
    public static Animation<TextureRegion> strafeRightAnimation;
    public static Animation<TextureRegion> strafeBackAnimation;

    protected Animation<TextureRegion> currentAnimation;

    public Survivor(ABNPGame gamePanel, String type, Vector2 startPos, int health, int maxHealth, int speed) {
        super(gamePanel, type, startPos, health, maxHealth, speed);

        WeaponModLoadout mods = new WeaponModLoadout();
        mods.addMod(new RedDotSight());
        weapons = new WeaponLoadout();
        weapons.getActiveWeapon().setMods(mods);
        aimTarget = new Vector2();
        currentAnimation = idleAnimation;

    }

    protected void drawRedDotSight(ShapeRenderer shape, Vector2 start, Vector2 end) {

        end.rotateAroundDeg(start, jitter);

        shape.setColor(.8f, 0f, 0f, .5f);
        shape.rectLine(start, end, 2);
        shape.circle(end.x, end.y, 3, 4);
        shape.setColor((float) (255.0/255.0), (float) (200.0/255.0), (float) (200.0/255.0), 1f);
        shape.circle(end.x, end.y, 1, 4);

    }
    public WeaponLoadout getWeaponLoadout() { return weapons; }
    public void setWeaponLoadout(WeaponLoadout newLoadout) {
        weapons = newLoadout;
    }

    public void setAnimFrame(int i) {
        animFrame = i;
    }

    public double getAimAngle() {
        return getAngleToPoint(aimTarget);
    }
    public Vector2 getAimVector() { return aimTarget; }
    public void setExp(int exp) { this.exp = exp; }
    public int getExp() { return exp; }


    public static void loadAssets(AssetManager assets) {
        // Sprites (Textures)
        assets.load("entity/player/legs/idle/Player_Legs_Idle.png", Texture.class);

        for (int i = 0; i < 20; i++) {
            assets.load("entity/player/legs/run/Player_Legs_Run_" + i + ".png", Texture.class);
        }

        for (int i = 0; i < 20; i++) {
            assets.load("entity/player/legs/strafe/left/Player_Legs_StrafeLeft_" + i + ".png", Texture.class);
        }

        for (int i = 0; i < 20; i++) {
            assets.load("entity/player/legs/strafe/right/Player_Legs_StrafeRight_" + i + ".png", Texture.class);
        }

        for (int i = 0; i < 20; i++) {
            assets.load("entity/player/legs/walk/Player_Legs_Walk_" + i + ".png", Texture.class);
        }

    }

    public static void loadSprites(AssetManager assets) {

        Array<TextureRegion> walkFrames = new Array<>();
        for (int i = 0; i < 20; i++) {
            walkFrames.add(new TextureRegion(assets.get("entity/player/legs/walk/Player_Legs_Walk_" + i + ".png", Texture.class)));
        }
        walkAnimation = new Animation<>(0.035f, walkFrames, Animation.PlayMode.LOOP);
        strafeBackAnimation = new Animation<>(0.045f, walkFrames, Animation.PlayMode.LOOP_REVERSED);

        Array<TextureRegion> idleFrames = new Array<>();
        idleFrames.add(new TextureRegion(assets.get("entity/player/legs/idle/Player_Legs_Idle.png", Texture.class)));
        idleAnimation = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);


        Array<TextureRegion> runFrames = new Array<>();
        for (int i = 0; i < 15; i++) {
            runFrames.add(new TextureRegion(assets.get("entity/player/legs/run/Player_Legs_Run_" + i + ".png", Texture.class)));
        }
        runAnimation = new Animation<>(0.025f, runFrames, Animation.PlayMode.LOOP);


        Array<TextureRegion> strafeLeftFrames = new Array<>();
        for (int i = 0; i < 20; i++) {
            strafeLeftFrames.add(new TextureRegion(assets.get("entity/player/legs/strafe/left/Player_Legs_StrafeLeft_" + i + ".png", Texture.class)));
        }
        strafeLeftAnimation = new Animation<>(0.03f, strafeLeftFrames, Animation.PlayMode.LOOP);


        Array<TextureRegion> strafeRightFrames = new Array<>();
        for (int i = 0; i < 20; i++) {
            strafeRightFrames.add(new TextureRegion(assets.get("entity/player/legs/strafe/right/Player_Legs_StrafeRight_" + i + ".png", Texture.class)));
        }
        strafeRightAnimation = new Animation<>(0.03f, strafeRightFrames, Animation.PlayMode.LOOP);

    }

}
