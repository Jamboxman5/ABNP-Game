package me.jamboxman5.abnpgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.UIManager;
import me.jamboxman5.abnpgame.util.Sounds;

public class GameScreen implements Screen, InputProcessor {
    final ABNPGame game;

    private final OrthographicCamera gameCamera;
    private final OrthographicCamera uiCamera;

    private float minZoom;
    private float maxZoom;

    long debugToggleTime;

    private final Vector3 touchPos = new Vector3();
    ShapeRenderer shape;

    public GameScreen(final ABNPGame game) {
        this.game = game;

        minZoom = .65f * (1920f/Gdx.graphics.getWidth());
        maxZoom = minZoom - .25f;
        shape = new ShapeRenderer();
        gameCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        debugToggleTime = System.currentTimeMillis();

        gameCamera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
        gameCamera.zoom = maxZoom;
        uiCamera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
        Gdx.input.setInputProcessor(this);

        game.getMapManager().setMap("Verdammtenstadt");
        UIManager.setupElements();

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

//        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
    }

    @Override
    public void show() {
        Sounds.AMBIENCE.stop();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
//        if (camera.zoom > .75f) camera.zoom = .75f;
        ScreenUtils.clear(.1f, 0, 0f, 1);

        // tell the camera to update its matrices.
        gameCamera.update();
        uiCamera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.shapeRenderer.setProjectionMatrix(gameCamera.combined);
        game.uiShapeRenderer.setProjectionMatrix(uiCamera.combined);
        game.uiCanvas.setProjectionMatrix(uiCamera.combined);
        // begin a new batch and draw the bucket and
        // all drops

        draw();


        update();

        // process user input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSLASH) && System.currentTimeMillis() - debugToggleTime > 100) {
            game.debugMode = !game.debugMode;
            debugToggleTime = System.currentTimeMillis();
        }
        game.getMapManager().getDebugRenderer().render(game.getMapManager().getWorld(), gameCamera.combined);
        game.getMapManager().getWorld().step(1/60f, 6, 2);
    }

    private void draw() {
        game.getMapManager().draw(game.batch, game.shapeRenderer);
        game.getPlayer().draw(game.batch, game.uiShapeRenderer);

        drawUI();

    }

    private void drawUI() {


        UIManager.drawWeaponHud(game.uiCanvas, game.uiShapeRenderer, game, gameCamera);
        UIManager.drawHealthBar(game.uiCanvas, game.uiShapeRenderer, game.getPlayer());
        if (game.debugMode) UIManager.drawDebugInfo(game, game.uiShapeRenderer, game.uiCanvas, Gdx.graphics.getDeltaTime());

    }

    private void update() {


        game.getPlayer().update();
        if (game.getPlayer().isMoving && getZoom() <= minZoom) {
            zoomOut();
        } else if (!game.getPlayer().isMoving && getZoom() > maxZoom) {
            zoomIn();
        }

        game.getMapManager().updateProjectiles();
    }

    @Override
    public void resize(int width, int height) {
        ScreenInfo.WIDTH = width;
        ScreenInfo.HEIGHT = height;
        game.batch.setProjectionMatrix(gameCamera.combined);

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
