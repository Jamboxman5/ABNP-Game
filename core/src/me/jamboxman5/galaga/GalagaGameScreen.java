package me.jamboxman5.galaga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GalagaGameScreen implements Screen {

    private float bulletspeed = 800;
    int level = 0;
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    final GalagaGame game;
    OrthographicCamera camera;
    Texture goodBulletImg;
    Texture badBulletImg;
    Texture enemyImg;
    Texture playerImg;
    Sound shootSound;
    Sound killSound;
    Sound deathSound;
    Sound attackSound;
    Rectangle player;
    Rectangle attacker;
    Vector3 touchPos;
    Vector2 attackPosition;
    Vector2 attackStartPosition;
    ArrayList<Rectangle> goodbullets;
    Array<Bullet> badbullets;
    Array<Rectangle> enemies;
    Array<Vector3> stars;
    long lastShootTime;
    long lastEnemyShootTime;
    long lastNewStar;
    long lastAttack;
    long lastDeath;
    int score;
    int lives;
    int enemyBulletCount;
    ShapeRenderer shapeRenderer;
    boolean reTarget = false;
    HashMap<Rectangle, Vector2> bulletTrajectories;
    HashMap<Rectangle, Vector2> bulletLaunches;

    public GalagaGameScreen (final GalagaGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        goodBulletImg = new Texture("galaga/goodbullet.png");
        badBulletImg = new Texture("galaga/badbullet.png");
        enemyImg = new Texture("galaga/enemy.png");
        playerImg = new Texture("galaga/player.png");

        shootSound = Gdx.audio.newSound(Gdx.files.internal("galaga/shoot.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("galaga/die.mp3"));
        killSound = Gdx.audio.newSound(Gdx.files.internal("galaga/kill.mp3"));
        attackSound = Gdx.audio.newSound(Gdx.files.internal("galaga/attack.mp3"));
        shapeRenderer = new ShapeRenderer();

        lastNewStar = System.currentTimeMillis();

        player = new Rectangle();
        player.width = 64;
        player.height = 64;
        player.x = 800/2 - player.width/2;
        player.y = 80;
        lives = 3;

        goodbullets = new ArrayList<Rectangle>();
        badbullets = new Array<Bullet>();
        enemies = new Array<Rectangle>();
        stars = new Array<Vector3>();

        spawnEnemies();
    }

    void spawnEnemies() {
//        Rectangle enemy = new Rectangle();
//        enemy.width = 64;
//        enemy.height = 64;
//        enemy.x = 64;
//        enemy.y = 600 -96;
//        enemies.add(enemy);

        if (!enemies.isEmpty()) return;

        for (int y = 600 - 128; y > 300; y -= 80) {
            for (int x = 64; x < 800-96; x += 96) {
                Rectangle enemy = new Rectangle();
                enemy.width = 64;
                enemy.height = 64;
                enemy.x = x;
                enemy.y = y;
                enemies.add(enemy);
            }
        }

        lastAttack = System.currentTimeMillis();
        level++;
    }

    void enemyShoot() {
        if (attacker == null) return;
        enemyBulletCount--;
        badbullets.add(new Bullet(new Vector2(attacker.x, attacker.y), new Vector2(player.x, player.y)));
        lastEnemyShootTime = System.currentTimeMillis();
    }

    private class Bullet {
        Vector2 start, end;
        float x, y;
        public Bullet(Vector2 startLoc, Vector2 endLoc) {
            start = startLoc;
            end = endLoc;
            x = start.x;
            y = start.y;
        }
        public boolean overlaps(Rectangle rect) {
            return new Rectangle(x, y, 64,64).overlaps(rect);
        }
        public void update() {
            y += (end.y - start.y) * getBulletSpeedModifier();
            x += (end.x - start.x) * Gdx.graphics.getDeltaTime();
        }
    }

    void enemyAttack() {
        if (attacker != null) {
            attacker.x += (attackPosition.x - attackStartPosition.x) * getSpeedModifier();
            attacker.y += (attackPosition.y - attackStartPosition.y) * getSpeedModifier();
        }

        if (System.currentTimeMillis() - lastDeath < 5000) return;
        if (enemies.isEmpty()) {
            if (System.currentTimeMillis() - lastAttack < 1500) return;
            spawnEnemies();
            return;
        }
        if (attacker == null) {
            enemyBulletCount = (int) Math.random() * 20;
            if (System.currentTimeMillis() - lastAttack < 1500) return;
            reTarget = retarget();
            attacker = enemies.get((int)(Math.random()*enemies.size));
            attackPosition = new Vector2(player.x, player.y);
            attackStartPosition = new Vector2(attacker.x, attacker.y);
            attackSound.stop();
            attackSound.play(.2f);
            enemyShoot();
            return;
        }
        if (System.currentTimeMillis() - lastEnemyShootTime > 200 && enemyBulletCount > 0) enemyShoot();

        if (attacker.y > 250) {
            attackPosition = new Vector2(player.x, player.y);
        }
        if (!attacker.overlaps(new Rectangle(0, 0, 800,600)))  {
            attacker.setPosition(attackStartPosition);
            attacker = null;
            lastAttack = System.currentTimeMillis();
            return;
        }
        if (attacker.overlaps(player)) {
            enemies.removeValue(attacker, true);
            attacker = null;
            attackSound.stop();
            lastAttack = System.currentTimeMillis();
            die();

        }


    }

    double getSpeedModifier() {
        return (1.0+(level/15.0)) / 60.0;
    }
    double getBulletSpeedModifier() {
        return 1.3*(1.0+(level/15.0)) / 60.0;
    }
    boolean retarget() {
        return ((Math.random()*10) + level > 15);
    }

    void renderStars() {
        Gdx.gl.glEnable(GL30.GL_BLEND);

        if (System.currentTimeMillis() - lastNewStar > 50) {
            lastNewStar = System.currentTimeMillis();
            int newX = (int) (Math.random()*800);
            int newY = (int) (Math.random()*600);

            stars.add(new Vector3(newX, newY, 1f));
        }

        Iterator<Vector3> iter = stars.iterator();
        while (iter.hasNext()) {
            Vector3 star = iter.next();
            shapeRenderer.setColor(new Color(1f, 1f,1f,star.z));

            shapeRenderer.rect(star.x, star.y, 4, 4);
            star.z -= .01;
            if (star.z < 0) {
                iter.remove();
            }
        }

    }

    void drawLives(SpriteBatch batch) {
        Sprite sprite = new Sprite(playerImg);
        sprite.scale(-.5f);

        int x = 32;
        for (int i = 0; i < lives; i++) {
            sprite.setCenter(x, 32);
            sprite.draw(batch);
            x += 40;
        }

    }

    private void spawnGoodBullet() {
        Rectangle bullet = new Rectangle();
        bullet.width = 32;
        bullet.height = 32;
        bullet.x = player.x;
        bullet.y = player.y;
        goodbullets.add(bullet);
        lastShootTime = System.currentTimeMillis();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderStars();
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        enemyAttack();
        drawLives(game.batch);

        game.font.draw(game.batch, "Score: "+ score, 360, 600-20);
        game.font.draw(game.batch, "Level "+ level, 800-64, 20);
        for (Rectangle bullet : goodbullets) {
            game.batch.draw(goodBulletImg, bullet.x, bullet.y);
        }
        for (Bullet bullet : badbullets) {
            game.batch.draw(badBulletImg, bullet.x, bullet.y);
        }
        for (Rectangle enemy : enemies) {
            game.batch.draw(enemyImg, enemy.x, enemy.y);
        }
        if (!isDead()) {
            game.batch.draw(playerImg, player.x, player.y);
        }
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.A) && !isDead()) {
            player.x -= 300 * Gdx.graphics.getDeltaTime();
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.D) && !isDead()) {
            player.x += 300 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shoot();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            spawnEnemies();
        }


        if (player.x < 0) player.x = 0;
        if (player.x > 800 - player.width) player.x = 800 - player.width;

        Array<Rectangle> bulletsToRemove = new Array<>();

        Iterator<Rectangle> iter = goodbullets.iterator();
        while (iter.hasNext()) {
            Rectangle bullet = iter.next();
            bullet.y += bulletspeed * Gdx.graphics.getDeltaTime();
            if (bullet.y > 800) {
                bulletsToRemove.add(bullet);
            }
            Iterator<Rectangle> enemyiter = enemies.iterator();
            while (enemyiter.hasNext()) {
                Rectangle enemy = enemyiter.next();
                if (bullet.overlaps(enemy)) {
                    enemyiter.remove();
                    bulletsToRemove.add(bullet);
                    killSound.play();
                    if (attacker == enemy) {
                        attacker = null;
                        lastAttack = System.currentTimeMillis();
                        attackSound.stop();

                    }
                    score += 100;
                }
            }

        }

        for (Rectangle r : bulletsToRemove) {
            goodbullets.remove(r);
        }


        Iterator<Bullet> badbulletiterator = badbullets.iterator();
        while (badbulletiterator.hasNext()) {
            Bullet bullet = badbulletiterator.next();

            bullet.update();

            if (bullet.y < -64) {
                badbulletiterator.remove();
            }
            if (bullet.overlaps(player)) {
                die();
                badbulletiterator.remove();

            }
        }
    }

    boolean isDead() {
        return (System.currentTimeMillis() - lastDeath < 2000);
    }

    private void die() {
        lives--;
        deathSound.play();
        player.x = 400-32;
        lastDeath = System.currentTimeMillis();
        if (lives < 0) game.setScreen(new GalagaGameOverScreen(game));
    }

    private void shoot() {
        if (isDead()) return;
        if (System.currentTimeMillis() - lastShootTime < 350) return;
        lastShootTime = System.currentTimeMillis();
        shootSound.play();
        spawnGoodBullet();
    }

    @Override
    public void show() {

    }

    @Override
    public void dispose () {
        playerImg.dispose();
        goodBulletImg.dispose();

        shootSound.dispose();
    }

    private class Enemy extends Rectangle {

    }
}