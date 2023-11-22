package me.jamboxman5.abnpgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SortedIntList;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class ABNPGame extends ApplicationAdapter {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private Rectangle bucket;
	private Array<Rectangle> rainDrops;
	private long lastDropTime;

	private Vector3 touchPos = new Vector3();

	@Override
	public void create () {

		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		dropImage = new Texture(Gdx.files.internal("droplet.png"));

		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);

		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 800/2 - 64/2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		rainDrops = new Array<>();
		spawnRainDrop();

	}

	private void spawnRainDrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		rainDrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, .2f, 1);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : rainDrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64/2;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800-64) bucket.x = 800-64;

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop();

		for (Iterator<Rectangle> iter = rainDrops.iterator(); iter.hasNext(); ) {
			Rectangle rainDrop = iter.next();
			rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (rainDrop.y + 64 < 0) iter.remove();
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
