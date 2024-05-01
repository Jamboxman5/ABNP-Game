package me.jamboxman5.abnpgame.entity.zombie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.Mob;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Fonts;

public class Zombie extends Mob {

    public static Array<Sprite> idleSprites;
    public static Array<Sprite> walkSprites;
    public static Array<Sprite> attackSprites;

    public static Sprite deadSprite;

    public Array<Sprite> activeSprites;

    protected Vector2 target;
    int animCounter = 0;

    public Zombie(ABNPGame game, ZombieType type, Vector2 startPos, int topSpeed) {
        super(game,
                type.toString(),
                (int) startPos.x,
                (int) startPos.y,
                50, 50,
                topSpeed);

        target = game.getPlayer().getPosition();



        activeSprites = attackSprites;



    }

    public static void initSprites() {
        deadSprite = setup("entity/zombie/misc/Splatter", null);
        idleSprites = new Array<>(new Sprite[]{setup("entity/zombie/default/idle/skeleton-idle_0", null),
                setup("entity/zombie/default/idle/skeleton-idle_1", null),
                setup("entity/zombie/default/idle/skeleton-idle_2", null),
                setup("entity/zombie/default/idle/skeleton-idle_3", null),
                setup("entity/zombie/default/idle/skeleton-idle_4", null),
                setup("entity/zombie/default/idle/skeleton-idle_5", null),
                setup("entity/zombie/default/idle/skeleton-idle_6", null),
                setup("entity/zombie/default/idle/skeleton-idle_7", null),
                setup("entity/zombie/default/idle/skeleton-idle_8", null),
                setup("entity/zombie/default/idle/skeleton-idle_9", null),
                setup("entity/zombie/default/idle/skeleton-idle_10", null),
                setup("entity/zombie/default/idle/skeleton-idle_11", null),
                setup("entity/zombie/default/idle/skeleton-idle_12", null),
                setup("entity/zombie/default/idle/skeleton-idle_13", null),
                setup("entity/zombie/default/idle/skeleton-idle_14", null),
                setup("entity/zombie/default/idle/skeleton-idle_15", null),
                setup("entity/zombie/default/idle/skeleton-idle_16", null)});
        walkSprites = new Array<>(new Sprite[]{setup("entity/zombie/default/move/skeleton-move_0", null),
                setup("entity/zombie/default/move/skeleton-move_1", null),
                setup("entity/zombie/default/move/skeleton-move_2", null),
                setup("entity/zombie/default/move/skeleton-move_3", null),
                setup("entity/zombie/default/move/skeleton-move_4", null),
                setup("entity/zombie/default/move/skeleton-move_5", null),
                setup("entity/zombie/default/move/skeleton-move_6", null),
                setup("entity/zombie/default/move/skeleton-move_7", null),
                setup("entity/zombie/default/move/skeleton-move_8", null),
                setup("entity/zombie/default/move/skeleton-move_9", null),
                setup("entity/zombie/default/move/skeleton-move_10", null),
                setup("entity/zombie/default/move/skeleton-move_11", null),
                setup("entity/zombie/default/move/skeleton-move_12", null),
                setup("entity/zombie/default/move/skeleton-move_13", null),
                setup("entity/zombie/default/move/skeleton-move_14", null),
                setup("entity/zombie/default/move/skeleton-move_15", null),
                setup("entity/zombie/default/move/skeleton-move_16", null)});
        attackSprites = new Array<>(new Sprite[]{setup("entity/zombie/default/attack/skeleton-attack_0", null),
                setup("entity/zombie/default/attack/skeleton-attack_1", null),
                setup("entity/zombie/default/attack/skeleton-attack_2", null),
                setup("entity/zombie/default/attack/skeleton-attack_3", null),
                setup("entity/zombie/default/attack/skeleton-attack_4", null),
                setup("entity/zombie/default/attack/skeleton-attack_5", null),
                setup("entity/zombie/default/attack/skeleton-attack_6", null),
                setup("entity/zombie/default/attack/skeleton-attack_7", null),
                setup("entity/zombie/default/attack/skeleton-attack_8", null)});
    }

    @Override
    public void update() {

        super.update();

        animCounter++;

        collision.setPosition(new Vector2(position.x, position.y).rotateAroundDeg(position, (float) (Math.toDegrees(getAngleToPoint(gp.getPlayer().getPosition())) + 360)));


        if (animCounter == 3) {
            animFrame++;

            Array<Sprite> lastSprites = activeSprites;

            if (velocity.len() > 2) {
                activeSprites = walkSprites;
            } else {
                if (collision.overlaps(gp.getPlayer().getCollision())) {
                    activeSprites = attackSprites;
                } else {
                    animFrame = 0;
                }
            }

            if (!lastSprites.equals(activeSprites)) animFrame = 0;

            if (animFrame == activeSprites.size) {
                animFrame = 0;
            }

            animCounter = 0;

        }

        arrive(new Vector2(gp.getPlayer().getCollision().x, gp.getPlayer().getCollision().y), 300, 1);
        if (collision.overlaps(gp.getPlayer().getCollision())) gp.getPlayer().damage(.1);

        if (isDead()) {
            gp.getPlayer().giveMoney(10);
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

        batch.setTransformMatrix(new Matrix4().translate((float) x, (float) y, 0).rotate(0f, 0f, 1f, (float) (Math.toDegrees(getAngleToPoint(target)) + 360) + jitter));
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
        NORMAL
    }

}
