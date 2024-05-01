package me.jamboxman5.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGame implements Screen {

    private float rainVelocity = 200;
    private float bucketVelocity = 1000;
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
        rainMusic.stop();
    }

    final Drop drop;
    OrthographicCamera camera;
    Texture dropImg;
    Texture bucketImg;
    Sound dropSound;
    Music rainMusic;
    Rectangle bucket;
    Vector3 touchPos;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGot;
    int dropsLost;

    public MyGame (final Drop drop) {
        this.drop = drop;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        dropImg = new Texture("droplet.png");
        bucketImg = new Texture("bucket.png");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));

        rainMusic.setLooping(true);
        rainMusic.play();

        bucket = new Rectangle();
        bucket.width = 64;
        bucket.height = 64;
        bucket.x = 800/2 - bucket.width/2;
        bucket.y = 20;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.width = 64;
        raindrop.height = 64;
        raindrop.x = MathUtils.random(0, 800 - raindrop.width);
        raindrop.y = 480;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        drop.batch.setProjectionMatrix(camera.combined);
        drop.batch.begin();
        drop.font.draw(drop.batch, "Gathered: "+dropsGot, 0, 480);
        drop.font.draw(drop.batch, "Lives: "+(10-dropsLost), 0, 460);
        for (Rectangle raindrop : raindrops) {
            drop.batch.draw(dropImg, raindrop.x, raindrop.y);
        }
        drop.batch.draw(bucketImg, bucket.x, bucket.y);
        drop.batch.end();

        if (Gdx.input.isTouched()) {
            touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            float v = (touchPos.x - (bucket.x+bucket.width/2))*4;
            if (v > bucketVelocity) v = bucketVelocity;
            if (v < -bucketVelocity) v = -bucketVelocity;
            bucket.x += v * Gdx.graphics.getDeltaTime();
        }
        else {
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
        }


        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 800 - bucket.width) bucket.x = 800 - bucket.width;

        if (TimeUtils.nanoTime() - lastDropTime >= 1000000000) {
            spawnRaindrop();
        }

        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= rainVelocity * Gdx.graphics.getDeltaTime();
            if (raindrop.y+raindrop.height < 0) {
                iter.remove();
                dropsLost++;
                bucketVelocity -= 50;
                if (dropsLost >= 10) drop.setScreen(new DropGameOverScreen(drop));
            }
            if (raindrop.overlaps(bucket)) {
                if (raindrop.y+raindrop.height/2 > bucket.height) {
                    dropsGot++;
                    rainVelocity += 10;
                    dropSound.play();
                    iter.remove();
                    if (dropsGot >= 100) drop.setScreen(new WinScreen(drop));

                }
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void dispose () {
        bucketImg.dispose();
        dropImg.dispose();

        dropSound.dispose();
        rainMusic.dispose();
    }
}