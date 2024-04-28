package me.jamboxman5.abnpgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private Viewport viewport;

    private final Vector3 touchPos = new Vector3();
    ShapeRenderer shape;

    public GameScreen(final ABNPGame game) {
        this.game = game;

        shape = new ShapeRenderer();
        gameCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        viewport = new FitViewport(ScreenInfo.WIDTH, ScreenInfo.HEIGHT, gameCamera);

        gameCamera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
        uiCamera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
        Gdx.input.setInputProcessor(this);

        game.getMapManager().setMap("Verdammtenstadt");
        UIManager.setupElements();
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
        game.getMapManager().getDebugRenderer().render(game.getMapManager().getWorld(), gameCamera.combined);
        game.getMapManager().getWorld().step(1/60f, 6, 2);
    }

    private void draw() {
        game.batch.begin();
        game.getMapManager().draw(game.batch);
        game.getPlayer().draw(game.batch, game.uiShapeRenderer);
        game.batch.end();

        game.uiCanvas.begin();
        drawUI();
        game.uiCanvas.end();

    }

    private void drawUI() {


        UIManager.drawWeaponHud(game.uiCanvas, game.uiShapeRenderer, game, gameCamera);

    }

    private void update() {


        game.getPlayer().update();
        if (game.getPlayer().isMoving && getZoom() <= 1.25) {
            zoomOut();
        } else if (!game.getPlayer().isMoving && getZoom() > 1) {
            zoomIn();
        }
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

    public void setZoom(float newZoom) { gameCamera.zoom = newZoom; }
    public float getZoom() { return gameCamera.zoom; }

    public void zoomIn() {
        setZoom(getZoom()-.002f);
    }
    public void zoomOut() {
        setZoom(getZoom()+.005f);
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
