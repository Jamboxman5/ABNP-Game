package me.jamboxman5.abnpgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.ally.Ally;
import me.jamboxman5.abnpgame.entity.zombie.Zombie;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.UIManager;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.screen.ui.screens.MainMenuScreen;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class GameScreen implements Screen, InputProcessor {
    final ABNPGame game;

    private final OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private float minZoom;
    private float maxZoom;


    long debugToggleTime;

    private final Vector3 touchPos = new Vector3();
    private long lastSpawn = System.currentTimeMillis();
    ShapeRenderer shape;

    int spawnCounter = 0;
    int spawnMultiplier = 1;

    Sound purchaseSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Purchase.wav"));
    Sound winSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Win.wav"));

    Thread gameController;
    int zombiesRemaining = 0;
    private boolean gameOver = false;

    public GameScreen(final ABNPGame game, Map activeMap) {
        this.game = game;

        minZoom = .65f * (1920f/Gdx.graphics.getWidth());
        maxZoom = minZoom - .25f;
//        minZoom = 1;
//        maxZoom = 1;
        shape = new ShapeRenderer();
        gameCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        debugToggleTime = System.currentTimeMillis();

        gameCamera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
        gameCamera.zoom = maxZoom;
        uiCamera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
        Gdx.input.setInputProcessor(this);

        game.getMapManager().setMap(activeMap);
//        UIManager.setupElements();



        Zombie.initSprites();



//        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
    }

    @Override
    public void show() {

        game.generatePlayer();
        game.getPlayer().setPosition(game.getMapManager().getActiveMap().getPlayerSpawn());
        game.getMapManager().addAlly(new Ally(game, "Sarge", game.getMapManager().getActiveMap().getPlayerSpawn().cpy().add(new Vector2(0,1)), 50, 50, 5));

        Sounds.AMBIENCE.stop();
        Pixmap pixmap;
        if (ScreenInfo.WIDTH > 1366) {
            pixmap = new Pixmap(Gdx.files.internal("ui/cursor/Cursor_Reticle_Large.png"));
            Cursor cursor = Gdx.graphics.newCursor(pixmap, 64, 64);
            Gdx.graphics.setCursor(cursor);
        } else {
            pixmap = new Pixmap(Gdx.files.internal("ui/cursor/Cursor_Reticle_Small.png"));
            Cursor cursor = Gdx.graphics.newCursor(pixmap, 32, 32);
            Gdx.graphics.setCursor(cursor);

        }

        gameController = new Thread() {

            @Override
            public void run() {
                try {

                    gameOver = false;

                    Vector2[] spawnPoints = game.getMapManager().getActiveMap().getZombieSpawns();
                    UIManager.pushBufferMessage("Prepare for the first wave!");
                    Thread.sleep(10000);
                    UIManager.pushBufferMessage("Begin!");

                    for (int i = 0; i < 10; i++) {
                        zombiesRemaining = (50 - i) + game.getMapManager().entities.size;
                        Thread.sleep(2000);
                        Zombie zombie = new Zombie(game, Zombie.ZombieType.NORMAL,spawnPoints[spawnCounter], 3);
                        game.getMapManager().addEntity(zombie);
                        lastSpawn = System.currentTimeMillis();
                        spawnCounter++;
                        if (spawnCounter >= spawnPoints.length) spawnCounter = 0;
                    }

                    while (game.getMapManager().entities.size > 0) {
                        zombiesRemaining = game.getMapManager().entities.size;
                        Thread.sleep(3000);
                    }
                    zombiesRemaining = game.getMapManager().entities.size;

                    winSound.play();
                    UIManager.pushBufferMessage("Prepare for the next wave!");
                    new Thread() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 150; i++) {
                                game.getPlayer().healBy(.1f, false);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    }.start();
                    Thread.sleep(15000);

                    UIManager.pushBufferMessage("Begin!");

                    for (int i = 0; i < 10; i++) {
                        zombiesRemaining = 2*(50 - i) + game.getMapManager().entities.size;
                        Thread.sleep(2000);
                        Zombie zombie2 = new Zombie(game, Zombie.ZombieType.NORMAL,spawnPoints[spawnCounter].cpy().add(new Vector2(40,40)), 4);
                        Zombie zombie3 = new Zombie(game, Zombie.ZombieType.NORMAL,spawnPoints[spawnCounter].cpy().add(new Vector2(-40,-40)), 4);
                        game.getMapManager().addEntity(zombie2);
                        game.getMapManager().addEntity(zombie3);
                        lastSpawn = System.currentTimeMillis();
                        spawnCounter++;
                        if (spawnCounter >= spawnPoints.length) spawnCounter = 0;
                    }

                    while (game.getMapManager().entities.size > 0) {
                        zombiesRemaining = game.getMapManager().entities.size;
                        Thread.sleep(3000);
                    }
                    zombiesRemaining = game.getMapManager().entities.size;

                    winSound.play();
                    UIManager.pushBufferMessage("Prepare for the final wave!");
                    new Thread() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 30; i++) {
                                game.getPlayer().healBy(.5, false);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    }.start();
                    Thread.sleep(15000);
                    UIManager.pushBufferMessage("Begin!");

                    for (int i = 0; i < 10; i++) {
                        zombiesRemaining = 3*(50 - i) + game.getMapManager().entities.size;
                        Thread.sleep(2000);
                        Zombie zombie = new Zombie(game, Zombie.ZombieType.NORMAL,spawnPoints[spawnCounter], 5);
                        Zombie zombie2 = new Zombie(game, Zombie.ZombieType.NORMAL,spawnPoints[spawnCounter].cpy().add(new Vector2(60,60)), 5);
                        Zombie zombie3 = new Zombie(game, Zombie.ZombieType.NORMAL,spawnPoints[spawnCounter].cpy().add(new Vector2(-60,-60)), 5);                        game.getMapManager().addEntity(zombie);
                        game.getMapManager().addEntity(zombie);
                        game.getMapManager().addEntity(zombie2);
                        game.getMapManager().addEntity(zombie3);
                        lastSpawn = System.currentTimeMillis();
                        spawnCounter++;
                        if (spawnCounter >= spawnPoints.length) spawnCounter = 0;
                    }

                    while (game.getMapManager().entities.size > 0) {
                        zombiesRemaining = game.getMapManager().entities.size;
                        Thread.sleep(3000);
                    }
                    zombiesRemaining = game.getMapManager().entities.size;

                    winSound.play();
                    UIManager.pushBufferMessage("Congratulations! You win!");
                    game.getMapManager().clearMap();
                    DataManager.save(game.getPlayer());
                    gameOver = true;


                } catch (InterruptedException e) {
                }
            }

        };

        gameController.start();

    }



    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
//        if (camera.zoom > .75f) camera.zoom = .75f;
        ScreenUtils.clear(0f, 0, 0f, 1);

        // tell the camera to update its matrices.
        gameCamera.update();
        uiCamera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.canvas.setProjectionMatrix(gameCamera.combined);
        game.shapeRenderer.setProjectionMatrix(gameCamera.combined);
        game.uiShapeRenderer.setProjectionMatrix(uiCamera.combined);
        game.uiCanvas.setProjectionMatrix(uiCamera.combined);
        // begin a new batch and draw the bucket and
        // all drops

        draw();


        update(delta);

        // process user input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSLASH) && System.currentTimeMillis() - debugToggleTime > 100) {
            game.debugMode = !game.debugMode;
            debugToggleTime = System.currentTimeMillis();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            if (System.currentTimeMillis() - lastSpawn > 100) {
                Zombie zombie = new Zombie(game, Zombie.ZombieType.NORMAL, game.getMapManager().getActiveMap().getZombieSpawns()[spawnCounter], 5);
                game.getMapManager().addEntity(zombie);
                lastSpawn = System.currentTimeMillis();
                spawnCounter++;
                if (spawnCounter >= game.getMapManager().getActiveMap().getZombieSpawns().length) spawnCounter = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            if (System.currentTimeMillis() - lastSpawn > 100) {

                if (game.getPlayer().getMoney() >= 100 && game.getPlayer().getWeaponLoadout().getActiveWeapon() instanceof Firearm) {
                    game.getPlayer().takeMoney(100);
                    ((Firearm)game.getPlayer().getWeaponLoadout().getActiveWeapon()).buyMag(1);
                    purchaseSound.play();
                }

                lastSpawn = System.currentTimeMillis();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            if (System.currentTimeMillis() - lastSpawn > 100) {

                UIManager.pushBufferMessage(zombiesRemaining + " Zombies Remaining!");

                lastSpawn = System.currentTimeMillis();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (System.currentTimeMillis() - lastSpawn > 100) {

                DataManager.save(game.getPlayer());
                game.getMapManager().clearMap();
                game.setScreen(new MainMenuScreen(game));
                dispose();

                lastSpawn = System.currentTimeMillis();
            }
        }

    }

    private void draw() {
        game.getMapManager().draw(game.canvas, game.shapeRenderer, gameCamera);
        game.getPlayer().draw(game.canvas, game.uiShapeRenderer);

        if (game.debugMode) {
            game.getPlayer().drawCollision(game.shapeRenderer);
        }

        drawUI();

    }


    private void drawUI() {


        UIManager.drawWeaponHud(game.uiCanvas, game.uiShapeRenderer, game, gameCamera);
        UIManager.drawHealthBar(game.uiCanvas, game.uiShapeRenderer, game.getPlayer());
        UIManager.drawMessageBuffer(game.uiCanvas);
        if (game.debugMode) UIManager.drawDebugInfo(game, game.uiShapeRenderer, game.uiCanvas, Gdx.graphics.getDeltaTime());
        if (gameOver) {
            if (UIManager.drawFadeOut(game.uiShapeRenderer)) {
                game.setScreen(new MainMenuScreen(game));
            }
        }

    }

    private void update(float delta) {


        game.getPlayer().update(delta);
        if (game.getPlayer().isMoving && getZoom() <= minZoom) {
            zoomOut();
        } else if (!game.getPlayer().isMoving && getZoom() > maxZoom) {
            zoomIn();
        }

        game.getMapManager().updateProjectiles();
        game.getMapManager().updateEntities(delta);
    }

    @Override
    public void resize(int width, int height) {
        ScreenInfo.WIDTH = width;
        ScreenInfo.HEIGHT = height;
        game.canvas.setProjectionMatrix(gameCamera.combined);

//        viewport.update(width, height);
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

    @Override
    public void dispose() {
//        game.setPlayer(null);
        game.getMapManager().clearMap();
        gameController.interrupt();

    }

    public void setZoom(float newZoom) {
        gameCamera.zoom = newZoom;
    }
    public float getZoom() { return gameCamera.zoom; }

    public void zoomIn() {
        setZoom(getZoom()-.001f);
    }
    public void zoomOut() {
        setZoom(getZoom()+.002f);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY >= 1) game.getPlayer().getWeaponLoadout().nextWeapon();
        if (amountY <= -1) game.getPlayer().getWeaponLoadout().previousWeapon();
        return false;
    }
}
