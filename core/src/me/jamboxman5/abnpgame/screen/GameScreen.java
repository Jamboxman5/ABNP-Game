package me.jamboxman5.abnpgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.mob.zombie.ZombieRunner;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.UIManager;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.net.packets.PacketWeaponChange;
import me.jamboxman5.abnpgame.screen.ui.screens.MainMenuScreen;
import me.jamboxman5.abnpgame.script.MissionScript;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

import java.io.IOException;

public class GameScreen implements Screen, InputProcessor {
    final ABNPGame game;

    private final OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;
    private final Viewport viewport;

    long debugToggleTime;

    private final Vector3 touchPos = new Vector3();
    ShapeRenderer shape;

    int spawnMultiplier = 1;

    Sound purchaseSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Purchase.wav"));


    MissionScript gameController;

    //3d

    private PerspectiveCamera perspectiveCamera;
    private ModelBatch modelBatch;
    private Environment environment;



    private DecalBatch decalBatch;


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
        viewport = new FitViewport(1280, 720, gameCamera);
//        UIManager.setupElements();

        gameController = controller;

        //3d world
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .8f, .8f, .8f, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -.8f, -.2f));


        //3d camera
        perspectiveCamera = new PerspectiveCamera(67, Settings.screenWidth, Settings.screenHeight);
        perspectiveCamera.near = 0.1f;
        perspectiveCamera.far = 600f;
        perspectiveCamera.position.y = Settings.maxZoom;
        decalBatch = new DecalBatch(new CameraGroupStrategy(perspectiveCamera));

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
//        gameController.start();

    }



    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
//        if (camera.zoom > .75f) camera.zoom = .75f;
        ScreenUtils.clear(0f, 0, 0f, 1);
        System.out.println(perspectiveCamera.position);

        // tell the camera to update its matrices.
        gameCamera.position.set(game.getPlayer().getWorldX(), game.getPlayer().getWorldZ(), 0);
        gameCamera.update();
        uiCamera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.canvas.setProjectionMatrix(perspectiveCamera.combined);
        game.shapeRenderer.setProjectionMatrix(perspectiveCamera.combined);
        game.shapeRenderer.rotate(1, 0, 0, -90);
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
                Zombie zombie = new ZombieRunner(game, game.getMapManager().getActiveMap().getZombieSpawns()[gameController.spawnCounter]);
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



        game.getMapManager().draw(environment, game.canvas, modelBatch, game.shapeRenderer, perspectiveCamera);
        game.getPlayer().draw(perspectiveCamera, decalBatch, game.uiShapeRenderer);

        if (game.debugMode) {
            game.getPlayer().drawCollision(game.shapeRenderer);
        }

        drawUI();

        decalBatch.flush();


    }


    private void drawUI() {


        UIManager.drawWeaponHud(game.uiCanvas, game.uiShapeRenderer, game, gameCamera);
        UIManager.drawHealthBar(game.uiCanvas, game.uiShapeRenderer, game.getPlayer());
        UIManager.drawRadar(game.uiCanvas, game.uiShapeRenderer, game);
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
            viewport.update(width, height, true);
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
        perspectiveCamera.position.y = newZoom;
    }
    public float getZoom() { return perspectiveCamera.position.y; }

    public void zoomIn() {
        setZoom(getZoom()-1f);
    }
    public void zoomOut() {
        setZoom(getZoom()+2f);
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
