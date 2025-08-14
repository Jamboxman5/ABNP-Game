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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.mob.zombie.ZombieNormal;
import me.jamboxman5.abnpgame.entity.prop.pickup.Pickup;
import me.jamboxman5.abnpgame.entity.prop.pickup.PickupWeapon;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.UIManager;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.net.packets.PacketWeaponChange;
import me.jamboxman5.abnpgame.screen.ui.screens.MainMenuScreen;
import me.jamboxman5.abnpgame.script.MissionScript;
import me.jamboxman5.abnpgame.util.InputKeys;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;

import java.io.IOException;

public class GameScreen implements Screen, InputProcessor {
    final ABNPGame game;

    private final OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    long debugToggleTime;

    private final Vector3 touchPos = new Vector3();
    ShapeRenderer shape;

    int spawnMultiplier = 1;

    Sound purchaseSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Purchase.wav"));

    MissionScript gameController;

    public GameScreen(final ABNPGame game, Map activeMap, MissionScript controller) {
        this.game = game;

        gameCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        debugToggleTime = System.currentTimeMillis();

        gameCamera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);
        gameCamera.zoom = Settings.maxZoom;
        uiCamera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);
        Gdx.input.setInputProcessor(this);

        game.getMapManager().setMap(activeMap);
//        UIManager.setupElements();

        gameController = controller;


//        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
    }

    @Override
    public void show() {

        game.getPlayer().setPosition(game.getMapManager().getActiveMap().getPlayerSpawn());

        Sounds.AMBIENCE.stop();
        Pixmap pixmap;
        if (Settings.screenWidth > 1366) {
            pixmap = new Pixmap(Gdx.files.internal("ui/cursor/Cursor_Reticle_Large.png"));
            Cursor cursor = Gdx.graphics.newCursor(pixmap, 64, 64);
            Gdx.graphics.setCursor(cursor);
        } else {
            pixmap = new Pixmap(Gdx.files.internal("ui/cursor/Cursor_Reticle_Small.png"));
            Cursor cursor = Gdx.graphics.newCursor(pixmap, 32, 32);
            Gdx.graphics.setCursor(cursor);

        }
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
            if (System.currentTimeMillis() - gameController.lastSpawn > 100) {
                Zombie zombie = new ZombieNormal(game, game.getMapManager().getActiveMap().getZombieSpawns()[gameController.spawnCounter]);
                game.getMapManager().addEntity(zombie);
                gameController.lastSpawn = System.currentTimeMillis();
                gameController.spawnCounter++;
                if (gameController.spawnCounter >= game.getMapManager().getActiveMap().getZombieSpawns().length) gameController.spawnCounter = 0;

            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            if (System.currentTimeMillis() - gameController.lastSpawn > 100) {

                if (game.getPlayer().getMoney() >= 100 && game.getPlayer().getWeaponLoadout().getActiveWeapon() instanceof Firearm) {
                    game.getPlayer().takeMoney(100);
                    ((Firearm)game.getPlayer().getWeaponLoadout().getActiveWeapon()).buyMag(1);
                    purchaseSound.play(Settings.sfxVolume);
                }

                gameController.lastSpawn = System.currentTimeMillis();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            if (System.currentTimeMillis() - gameController.lastSpawn > 100) {

                UIManager.pushBufferMessage(gameController.zombiesRemaining + " Zombies Remaining!");

                gameController.lastSpawn = System.currentTimeMillis();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if (System.currentTimeMillis() - gameController.lastSpawn > 100) {

                DataManager.save(game.getPlayer());
                game.getMapManager().clearMap();
                game.setScreen(new MainMenuScreen(game));
                dispose();

                gameController.lastSpawn = System.currentTimeMillis();
                if (game.isMultiplayer()) try {
                    game.closeMultiplayerGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        if (gameController.gameOver) {
            if (UIManager.drawFadeOut(game.uiShapeRenderer)) {
                game.setScreen(new MainMenuScreen(game));
            }
        }

    }

    private void update(float delta) {


        game.getPlayer().update(delta);
        if (game.getPlayer().isMoving && getZoom() <= Settings.minZoom) {
            zoomOut();
        } else if (!game.getPlayer().isMoving && getZoom() > Settings.maxZoom) {
            zoomIn();
        }

        game.getMapManager().updateProjectiles();
        game.getMapManager().updateEntities(delta);
    }

    @Override
    public void resize(int width, int height) {
//        Settings.screenWidth = width;
//        Settings.screenHeight = height;
//        game.canvas.setProjectionMatrix(gameCamera.combined);

//        viewport.update(width, height);
    }

    @Override
    public void pause() {
        gameController.interrupt();

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        gameController.interrupt();

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
        if (game.isMultiplayer()) {
            PacketWeaponChange packet = new PacketWeaponChange();
            packet.uuid = game.getPlayer().getID();
            packet.type = game.getPlayer().getWeaponLoadout().getActiveWeapon().getType();
            game.getClientManager().sendPacketTCP(packet);
        }
        return false;
    }
}
