package me.jamboxman5.abnpgame.entity.mob.zombie;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.mob.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;

public class Zombie extends Mob {

    public static Array<Sprite> idleSprites;
    public static Array<Sprite> walkSprites;
    public static Array<Sprite> attackSprites;

    public static Sprite deadSprite;

    public Array<Sprite> activeSprites;

    int animCounter = 0;
    protected double damage;
    protected long lastHit = 0;
    protected int attackCooldownMS;
    protected int rewardMoney;
    protected int rewardEXP;

    public Zombie(ABNPGame game,
                  ZombieType type,
                  Vector2 startPos,
                  int topSpeed,
                  int maxHealth,
                  int attackCooldownMS,
                  int rewardMoney,
                  int rewardEXP,
                  double damage) {
        super(game,
                type.toString(),
                startPos,
                maxHealth, maxHealth,
                topSpeed);

        target = game.getPlayer().getPosition();
        this.damage = damage;
        this.attackCooldownMS = attackCooldownMS;
        this.rewardMoney = rewardMoney;
        this.rewardEXP = rewardEXP;

        activeSprites = attackSprites;



    }

    public static void loadAssets(AssetManager assets) {
        assets.load("entity/zombie/misc/Splatter.png", Texture.class);

        for (int i = 0; i <= 16; i++) {
            assets.load("entity/zombie/default/idle/skeleton-idle_" + i + ".png", Texture.class);
        }

        for (int i = 0; i <= 16; i++) {
            assets.load("entity/zombie/default/move/skeleton-move_" + i + ".png", Texture.class);
        }

        for (int i = 0; i <= 8; i++) {
            assets.load("entity/zombie/default/attack/skeleton-attack_" + i + ".png", Texture.class);
        }
    }

    public static void loadSprites(AssetManager assets) {

        // Dead sprite
        deadSprite = setup("entity/zombie/misc/Splatter.png", assets, defaultSpriteScale);

        // Idle sprites
        idleSprites = new Array<>();
        for (int i = 0; i <= 16; i++) {
            idleSprites.add(new Sprite(
                    setup("entity/zombie/default/idle/skeleton-idle_" + i + ".png", assets, defaultSpriteScale)
            ));
        }

        // Walk sprites
        walkSprites = new Array<>();
        for (int i = 0; i <= 16; i++) {
            walkSprites.add(new Sprite(
                    setup("entity/zombie/default/move/skeleton-move_" + i + ".png", assets, defaultSpriteScale)
            ));
        }

        // Attack sprites
        attackSprites = new Array<>();
        for (int i = 0; i <= 8; i++) {
            attackSprites.add(new Sprite(
                    setup("entity/zombie/default/attack/skeleton-attack_" + i + ".png", assets, defaultSpriteScale)
            ));
        }
    }

    @Override
    public void update(float delta) {

        super.update(delta);

        animCounter++;

        ((Circle)collision).setPosition(new Vector2(position.x, position.y).rotateAroundDeg(position, (float) (Math.toDegrees(getAngleToPoint(gp.getPlayer().getPosition())) + 360)));


        if (animCounter == 3) {
            animFrame++;
            Array<Sprite> lastSprites = activeSprites;

            if (velocity.len() > 0) {
                activeSprites = walkSprites;
                if (getCollision().overlaps(gp.getPlayer().getCollision())) {
                    activeSprites = attackSprites;
                }
            } else {
                if (((Circle)collision).overlaps(gp.getPlayer().getCollision())) {
                    activeSprites = attackSprites;
                } else {
                    activeSprites = idleSprites;
                }
            }


            if (!lastSprites.equals(activeSprites)) animFrame = 0;

            if (animFrame == activeSprites.size) {
                animFrame = 0;
            }

            animCounter = 0;

        }

        setRotation((float) (Math.toDegrees(getAngleToPoint(target)) + 360) + jitter);

        arrive(new Vector2(gp.getPlayer().getCollision().x, gp.getPlayer().getCollision().y), 300, 1);
        if (getCollision().overlaps(gp.getPlayer().getCollision())) gp.getPlayer().damage(damage);

        if (isDead()) {
            gp.getPlayer().giveMoney(rewardMoney);
            gp.getPlayer().giveExp(rewardEXP);
            gp.getMapManager().disposingEntities.add(this);
            gp.getMapManager().addSplatter(position);
        }

    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        int x = (int) (((position.x - gp.getPlayer().getWorldX())*.5) + gp.getPlayer().getScreenX());
        int y = (int) (((position.y - gp.getPlayer().getWorldY())*.5) + gp.getPlayer().getScreenY());

        batch.begin();
        Sprite toDraw = activeSprites.get(animFrame);

        batch.setTransformMatrix(new Matrix4().translate((float) x, (float) y, 0).rotate(0f, 0f, 1f, getRotation()));
        toDraw.setPosition((-toDraw.getWidth() / 2), (-toDraw.getHeight() / 2));
        toDraw.draw(batch);
        batch.setTransformMatrix(new Matrix4());


        batch.end();


    }



    @Override
    public boolean hasCollided(double xComp, double yComp) {
        return false;
    }

    public enum ZombieType {
        TANK, RUNNER, NORMAL
    }

}
