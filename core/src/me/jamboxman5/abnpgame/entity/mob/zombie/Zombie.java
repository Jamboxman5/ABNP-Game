package me.jamboxman5.abnpgame.entity.mob.zombie;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    protected Decal zombieDecal;

    private boolean isAttacking = false;
    private PursuitType pursuitType;
    public Zombie(ABNPGame game,
                  ZombieType type,
                  Vector3 startPos,
                  float topSpeed,
                  int maxHealth,
                  int attackCooldownMS,
                  int rewardMoney,
                  int rewardEXP,
                  double damage,
                  PursuitType pursuitType) {
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
        this.pursuitType = pursuitType;

        activeSprites = attackSprites;

        zombieDecal = Decal.newDecal(new TextureRegion(activeSprites.get(0).getTexture()), true);

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

        ((Circle)collision).setPosition(new Vector2(position.x, position.z).rotateAroundDeg(new Vector2(position.x, position.z), (float) (Math.toDegrees(getAngleToPoint(gp.getPlayer().getPosition())) + 360)));
//        ((Circle)collision).setPosition(new Vector2(position.x, position.z));


        if (animCounter == 3) {
            animFrame++;
            Array<Sprite> lastSprites = activeSprites;

            if (isAttacking) {
                activeSprites = attackSprites;

                if (animFrame >= attackSprites.size) {
                    animFrame = 0;
                    isAttacking = false;
                }
            } else {
                if (velocity.len() > 0) {
                    activeSprites = walkSprites;
                } else {
                    activeSprites = idleSprites;
                }
            }

            if (!lastSprites.equals(activeSprites)) animFrame = 0;

            if (animFrame >= activeSprites.size) {
                animFrame = 0;
            }

            animCounter = 0;
        }

        setRotation((float) (Math.toDegrees(getAngleToPoint(target)) + 360) + jitter);

        if (!isAttacking) {
            switch (pursuitType) {
                case SEEK: {
                    seek(new Vector3(gp.getPlayer().getCollision().x, gp.getPlayer().getPosition().y, gp.getPlayer().getCollision().y));
                    break;
                }
                case PURSUE: {
                    pursue(gp.getPlayer());
                    break;
                }
                case ARRIVE: {
                    arrive(new Vector3(gp.getPlayer().getCollision().x, gp.getPlayer().getPosition().y, gp.getPlayer().getCollision().y), 250, 1);
                    break;
                }
            }
        }

        if (getCollision().overlaps(gp.getPlayer().getCollision()) && System.currentTimeMillis() - lastHit > attackCooldownMS) {
            gp.getPlayer().damage(damage);
            lastHit = System.currentTimeMillis();

            isAttacking = true;
            velocity.limit(0);
            acceleration.limit(0);
            animFrame = 0;
        }

        if (isDead()) {
            gp.getPlayer().giveMoney(rewardMoney);
            gp.getPlayer().giveExp(rewardEXP);
            gp.getMapManager().disposingEntities.add(this);
            gp.getMapManager().addSplatter(position);
        }

    }

    @Override
    public void draw(DecalBatch batch, ShapeRenderer shape, PerspectiveCamera camera) {

        TextureRegion frame = new TextureRegion(activeSprites.get(animFrame).getTexture());
        zombieDecal.setTextureRegion(frame);

        zombieDecal.setWidth(frame.getRegionWidth());
        zombieDecal.setHeight(frame.getRegionHeight());

        zombieDecal.setPosition(position.x, position.y, -position.z);

        zombieDecal.translateY(60);
        zombieDecal.lookAt(camera.position, camera.up);

        zombieDecal.setScale(.25f);

        zombieDecal.rotateZ(getRotation());

        batch.add(zombieDecal);

    }



    @Override
    public boolean hasCollided(double xComp, double yComp) {
        return false;
    }

    public enum ZombieType {
        TANK, RUNNER, NORMAL
    }

}
